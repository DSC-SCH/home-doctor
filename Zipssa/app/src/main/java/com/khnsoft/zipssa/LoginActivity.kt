package com.khnsoft.zipssa

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.account_login_activity.*

class LoginActivity : AppCompatActivity() {
	companion object {
		const val SP_NAME = "account"
		const val SP_LOGIN = "login"
		const val LOGIN_INIT = -1
		const val LOGIN_NONE = 0
		const val LOGIN_ID = 1
		const val LOGIN_KAKAO = 2
		const val LOGIN_GOOGLE = 3
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.account_login_activity)

		val sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
		val editor = sp.edit()

		// TODO("Remove above line")
		startMain()

		/*
		if (sp.getInt(SP_LOGIN, LOGIN_INIT) != LOGIN_INIT) {
			startMain()
		}
		// */

		no_login_btn.setOnClickListener {
			editor.putInt(SP_LOGIN, LOGIN_NONE)
			editor.apply()
			startMain()
		}

		join_btn.setOnClickListener {
			val intent = Intent(this@LoginActivity, JoinActivity::class.java)
			startActivity(intent)
		}

		// TODO("Social login")

		login_kakao_btn.setOnClickListener {
			login_kakao_origin_btn.performClick()
		}

		login_google_btn.setOnClickListener {
			login_google_origin_btn.performClick()
		}
	}

	fun startMain() {
		val intent = Intent(this@LoginActivity, MainActivity::class.java)
		startActivity(intent)
		finish()
	}
}
