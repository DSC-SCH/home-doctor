package com.designvalue.medihand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
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
import kotlinx.android.synthetic.main.account_login_activity.*
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
	companion object {
		var curActivity: LoginActivity? = null
		const val SP_LOGIN = "login"
		const val SP_USER_ID = "user_id"
		const val SP_USER_NAME = "user_name"
		const val SP_HELP = "help"

		val RC_GOOGLE = 9001
	}

	lateinit var mGoogleSignInClient: GoogleSignInClient
	val handler = Handler()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.account_login_activity)

		curActivity = this@LoginActivity

		val sp = SPHandler.getSp(this@LoginActivity)
		val editor = sp.edit()

		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(getString(R.string.default_web_client_id))
			.requestEmail()
			.build()
		mGoogleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)

		//*
		val accountType = AccountType.valueOf(sp.getString(SP_LOGIN, AccountType.NO_LOGIN.name)!!)
		if (accountType != AccountType.NO_LOGIN) {
			MyLogger.i("Login Type", accountType.name)
			when (accountType) {
				AccountType.OFFLINE -> {
					UserData.accountType = AccountType.OFFLINE
					UserData.id = UserData.DEFAULT_ID
					startLoading()
				}
				AccountType.KAKAO -> {
					val result = ServerHandler.send(this@LoginActivity, EndOfAPI.CHECK_INTERNET)
					if (!HttpHelper.isOK(result)) {
						Toast.makeText(
							this@LoginActivity,
							"인터넷 연결이 없어 오프라인으로 로그인합니다. 오프라인 상태에서는 정보가 부정확할 수 있습니다.",
							Toast.LENGTH_SHORT
						).show()
						UserData.id = sp.getInt(SP_USER_ID, UserData.DEFAULT_ID)
						UserData.accountType = AccountType.NO_NETWORK
						startLoading()
						return
					}
					val session = Session.getCurrentSession()
					session.addCallback(SessionCallback(this@LoginActivity))
					session.open(AuthType.KAKAO_TALK, this@LoginActivity)
					loadingOn()
				}
				AccountType.GOOGLE -> {
					val result = ServerHandler.send(this@LoginActivity, EndOfAPI.CHECK_INTERNET)
					if (!HttpHelper.isOK(result)) {
						Toast.makeText(
							this@LoginActivity,
							"인터넷 연결이 없어 오프라인으로 로그인합니다. 오프라인 상태에서는 정보가 부정확할 수 있습니다.",
							Toast.LENGTH_SHORT
						).show()
						UserData.id = sp.getInt(SP_USER_ID, UserData.DEFAULT_ID)
						UserData.accountType = AccountType.NO_NETWORK
						startLoading()
						return
					}
					val task = mGoogleSignInClient.silentSignIn()
					if (task.isSuccessful) {
						val account = task.result
						validGoogle(account ?: return)
						loadingOn()
					} else {
						signIn()
						loadingOn()
					}
				}
				else -> {
				}
			}
			handler.postDelayed({
				loadingOff()
			}, 2000)
		} else {
			loadingOff()
		}

		offline_btn.setOnClickListener {
			val sp = SPHandler.getSp(this@LoginActivity)
			val editor = sp.edit()

			editor.putString(SP_LOGIN, AccountType.OFFLINE.name)
			editor.apply()
			UserData.accountType = AccountType.OFFLINE
			UserData.id = UserData.DEFAULT_ID

			val json = JsonObject()
			json.addProperty("user_id", UserData.id)
			json.addProperty("user_name", "약타미")

			ServerHandler.send(this@LoginActivity, EndOfAPI.LOCAL_USER_REGISTER, json)
			startLoading()
		}

		login_kakao_btn.setOnClickListener {
			val session = Session.getCurrentSession()
			session.addCallback(SessionCallback(this@LoginActivity))
			session.open(AuthType.KAKAO_TALK, this@LoginActivity)
			loadingOn()
		}

		login_google_btn.setOnClickListener {
			signIn()
			loadingOn()
		}

		if (!sp.getBoolean(SP_HELP, false)) {
			editor.putBoolean(SP_HELP, true)
			editor.apply()
			val intent = Intent(this@LoginActivity, MypageHelpActivity::class.java)
			startActivity(intent)
		}
	}

	fun startLoading() {
		handler.removeCallbacksAndMessages(null)
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
			val task = GoogleSignIn.getSignedInAccountFromIntent(data)
			try {
				val account = task.getResult(ApiException::class.java)
				firebaseAuthWithGoogle(account)
			} catch (e: Exception) {
				e.printStackTrace()
			}
		} else {
			if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
				return;
			}
		}
	}

	fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
		if (acct == null) {
			MyLogger.e("Google Login", "Account is null")
			return
		}

		val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
		FirebaseAuth.getInstance().signInWithCredential(credential)
			.addOnCompleteListener {
				if (it.isSuccessful) {
					if (acct.id == null) {
						MyLogger.e("Google Login", "Token is null")
						loadingOff()
						return@addOnCompleteListener
					}

					validGoogle(acct)
				} else {
					MyLogger.e("Google Login", "Sign in failed")
					loadingOff()
				}
			}
	}

	fun validGoogle(acct: GoogleSignInAccount) {
		val json = JsonObject()
		json.addProperty("snsType", AccountType.GOOGLE.name)
		json.addProperty("snsId", acct.id)
		val loginResult = ServerHandler.send(null, EndOfAPI.USER_LOGIN, json)
		val sp = SPHandler.getSp(this@LoginActivity)
		val editor = sp.edit()

		when (loginResult["status"].asInt) {
			HttpHelper.OK_CODE -> {
				val userData = loginResult["data"].asJsonObject
				UserData.accountType = AccountType.GOOGLE
				UserData.token = userData["token"].asString
				UserData.id = userData["userId"].asInt
				UserData.name = userData["username"].asString

				editor.putString(AlarmReceiver.SP_TOKEN, UserData.token)
				editor.apply()
				if (sp.getInt(SP_USER_ID, UserData.DEFAULT_ID) != UserData.id) {
					editor.putInt(SP_USER_ID, UserData.id)
					editor.putString(SP_USER_NAME, UserData.name)
					editor.apply()
				}
				if (sp.getString(SP_LOGIN, UserData.DEFAULT_ACCOUNT.name) != AccountType.GOOGLE.name) {
					editor.putString(SP_LOGIN, AccountType.GOOGLE.name)
					editor.apply()
				}

				startLoading()
			}
			HttpHelper.NO_USER_CODE -> {
				val intent = Intent(this@LoginActivity, JoinActivity::class.java)
				intent.putExtra("sns_type", AccountType.GOOGLE.name)
				intent.putExtra("sns_id", acct.id)
				intent.putExtra("name", "${acct.familyName}${acct.givenName}")
				intent.putExtra("email", acct.email)
				startActivity(intent)
				loadingOff()
			}
			else -> {
				if ((sp.getString(AlarmReceiver.SP_TOKEN, "") ?: "").isBlank()) {
					Toast.makeText(this@LoginActivity, "최소 한 번은 인터넷이 연결된 환경에서 실행해야 합니다", Toast.LENGTH_SHORT).show()
					loadingOff()
					return
				}
				UserData.accountType = AccountType.NO_NETWORK
				startLoading()
			}
		}
	}

	fun loadingOn() {
		loading.visibility = View.VISIBLE
	}

	fun loadingOff() {
		loading.visibility = View.GONE
	}
}
