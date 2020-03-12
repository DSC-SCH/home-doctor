package com.khnsoft.zipssa

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.JsonObject
import com.kakao.auth.AuthType
import com.kakao.auth.Session
import com.khnsoft.zipssa.KakaoLogin.SessionCallback
import kotlinx.android.synthetic.main.account_login_activity.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {
	companion object {
		const val SP_LOGIN = "login"
		const val SP_USER_ID = "user_id"

		val RC_GOOGLE = 9001
	}

	lateinit var mGoogleSignInClient : GoogleSignInClient

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.account_login_activity)

		val sp = getSharedPreferences(SharedPreferencesSrc.SP_NAME, Context.MODE_PRIVATE)
		val editor = sp.edit()

		offline_btn.setOnClickListener {
			editor.putString(SP_LOGIN, AccountType.OFFLINE.type)
			editor.apply()
			UserData.accountType = AccountType.OFFLINE
			UserData.id = sp.getInt(SP_USER_ID, UserData.DEFAULT_ID)

			val sdf_date_save = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
			val curCal = Calendar.getInstance()

			val json = JsonObject()
			json.addProperty("user_id", UserData.id)
			json.addProperty("user_name", "약타미")
			json.addProperty("created_date", sdf_date_save.format(curCal.time))
			json.addProperty("last_modified_date", sdf_date_save.format(curCal.time))

			ServerHandler.send(this@LoginActivity, EndOfAPI.USER_REGISTER, json)
			startLoading()
		}

		login_kakao_btn.setOnClickListener {
			val session = Session.getCurrentSession()
			session.addCallback(SessionCallback(this@LoginActivity))
			session.open(AuthType.KAKAO_TALK, this@LoginActivity)
		}

		login_google_btn.setOnClickListener {
			signIn()
		}

		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(getString(R.string.default_web_client_id))
			.requestEmail()
			.build()
		mGoogleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)

		// TODO("Remove above line")
		// startLoading()

		//*
		val accountType = AccountType.valueOf(sp.getString(SP_LOGIN, AccountType.NO_LOGIN.type)!!)
		if (accountType != AccountType.NO_LOGIN) {
			Log.i("@@@", accountType.type)
			when (accountType) {
				AccountType.OFFLINE -> {
					UserData.accountType = AccountType.OFFLINE
					UserData.id = sp.getInt(SP_USER_ID, UserData.DEFAULT_ID)
				}
				AccountType.KAKAO -> {
					val session = Session.getCurrentSession()
					session.addCallback(SessionCallback(this@LoginActivity))
					session.open(AuthType.KAKAO_TALK, this@LoginActivity)
				}
				AccountType.GOOGLE -> {
					val task = mGoogleSignInClient.silentSignIn()
					if (task.isSuccessful) {
						val account = task.getResult()
						validGoogle(account?:return)
					}
				}
				else -> {}
			}
			startLoading()
		}
		// */
	}

	fun startLoading() {
		val intent = Intent(this@LoginActivity, LoadingActivity::class.java)
		startActivity(intent)
		finish()
	}

	fun signIn() {
		val signInIntent = mGoogleSignInClient.signInIntent
		startActivityForResult(signInIntent, RC_GOOGLE)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == RC_GOOGLE) {
			Log.i("@@@", "RC_GOOGLE")
			val task = GoogleSignIn.getSignedInAccountFromIntent(data)
			try {
				val account = task.getResult(ApiException::class.java)
				firebaseAuthWithGoogle(account)
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}
	}

	fun firebaseAuthWithGoogle(acct : GoogleSignInAccount?) {
		if (acct == null) {
			Log.i("Google Login", "Account is null")
			return
		}

		val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
		FirebaseAuth.getInstance().signInWithCredential(credential)
			.addOnCompleteListener {
				if (it.isSuccessful) {
					if (acct.id == null) {
						Log.i("Google Login", "Token is null")
						return@addOnCompleteListener
					}

					validGoogle(acct)
				} else {
					Log.i("@@@", "Sign in failed")
				}
			}
	}

	fun validGoogle(acct: GoogleSignInAccount) {
		val json = JsonObject()
		json.addProperty("snsType", AccountType.GOOGLE.type)
		json.addProperty("snsId", acct.id)
		val loginResult = ServerHandler.send(null, EndOfAPI.USER_LOGIN, json)

		when (loginResult["status"].asInt) {
			HttpHelper.OK_CODE -> {
				val userData = loginResult["data"].asJsonObject
				UserData.accountType = AccountType.GOOGLE
				UserData.token = userData["token"].asString
				UserData.id = userData["userId"].asInt

				val sp = getSharedPreferences(SharedPreferencesSrc.SP_NAME, Context.MODE_PRIVATE)
				if (sp.getInt(SP_USER_ID, UserData.DEFAULT_ID) != UserData.id) {
					val editor = sp.edit()
					editor.putInt(SP_USER_ID, UserData.id)
					editor.apply()
				}

				startLoading()
			}
			HttpHelper.NO_USER_CODE -> {
				val intent = Intent(this@LoginActivity, JoinActivity::class.java)
				intent.putExtra("sns_type", AccountType.GOOGLE.type)
				intent.putExtra("sns_id", acct.id)
				intent.putExtra("name", "${acct.familyName}${acct.givenName}")
				intent.putExtra("email", acct.email)
				startActivity(intent)
				finish()
			}
			else -> {
				UserData.accountType = AccountType.NO_NETWORK
				startLoading()
			}
		}
	}
}
