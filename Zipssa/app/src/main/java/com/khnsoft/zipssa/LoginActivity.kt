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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.auth.AuthType
import com.kakao.auth.Session
import com.khnsoft.zipssa.KakaoLogin.SessionCallback
import kotlinx.android.synthetic.main.account_login_activity.*
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
	companion object {
		const val SP_LOGIN = "login"
		const val LOGIN_INIT = -1
		const val LOGIN_NONE = 0
		const val LOGIN_ID = 1
		const val LOGIN_KAKAO = 2
		const val LOGIN_GOOGLE = 3

		val REQUEST_CODE_GOOGLE = 9001
	}

	lateinit var mAuth : FirebaseAuth
	lateinit var mGoogleSignInClient : GoogleSignInClient

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.account_login_activity)

		val sp = getSharedPreferences(SharedPreferencesSrc.SP_ACCOUNT_NAME, Context.MODE_PRIVATE)
		val editor = sp.edit()

		// TODO("Remove above line")
		// startLoading()

		/*
		if (sp.getInt(SP_LOGIN, LOGIN_INIT) != LOGIN_INIT) {
			startMain()
		}
		// */

		no_login_btn.setOnClickListener {
			editor.putInt(SP_LOGIN, LOGIN_NONE)
			editor.apply()
			startLoading()
		}

		join_btn.setOnClickListener {
			val intent = Intent(this@LoginActivity, JoinActivity::class.java)
			startActivity(intent)
		}

		login_kakao_btn.setOnClickListener {
			val session = Session.getCurrentSession()
			session.addCallback(SessionCallback())
			session.open(AuthType.KAKAO_TALK, this@LoginActivity)
		}

		login_google_btn.setOnClickListener {
			signIn()
		}

		// TODO("Social login")
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(getString(R.string.default_web_client_id))
			.requestEmail()
			.build()
		mGoogleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)
	}

	fun startLoading() {
		val intent = Intent(this@LoginActivity, LoadingActivity::class.java)
		startActivity(intent)
		finish()
	}

	fun signIn() {
		val signInIntent = mGoogleSignInClient.signInIntent
		startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == REQUEST_CODE_GOOGLE) {
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
			return
		}

		val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
		FirebaseAuth.getInstance().signInWithCredential(credential)
			.addOnCompleteListener {
				if (it.isSuccessful) {
					if (acct.idToken == null) {
						return@addOnCompleteListener
					}
					if (ServerHandler.isUserExists(AccountType.GOOGLE, acct.idToken!!)) {
						// TODO("Login")
					} else {
						// TODO("Register")
					}
				} else {
					Log.i("@@@", "Sign in failed")
				}
			}
	}
}
