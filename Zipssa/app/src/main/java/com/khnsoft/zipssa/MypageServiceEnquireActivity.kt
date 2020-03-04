package com.khnsoft.zipssa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.mypage_service_enquire_activity.*

class MypageServiceEnquireActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_service_enquire_activity)

		back_btn.setOnClickListener { onBackPressed() }

		confirm_btn.setOnClickListener {
			if (checkEmail(email_input.text.toString())) {
				// TODO("Send enquire to server")
			} else {
				Toast.makeText(this@MypageServiceEnquireActivity, "회신 이메일 주소를 확인해 주세요", Toast.LENGTH_LONG).show()
			}
		}
	}

	fun checkEmail(email: String) : Boolean {
		val et_idx = email.indexOf('@')
		val dot_idx = email.indexOf('.')

		return when {
			// No '@'
			et_idx == -1 -> false
			// No '.'
			dot_idx == -1 -> false
			// No letters before '@'
			et_idx == 0 -> false
			// No letters between '@' and '.'
			dot_idx == et_idx + 1 -> false
			// No letters after '.'
			email.length == dot_idx + 1 -> false
			else -> true
		}
	}
}
