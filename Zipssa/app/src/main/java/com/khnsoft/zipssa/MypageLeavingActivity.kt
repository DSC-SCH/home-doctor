package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import kotlinx.android.synthetic.main.mypage_leaving_activity.*
import kotlinx.android.synthetic.main.mypage_setting_activity.*
import kotlinx.android.synthetic.main.mypage_setting_activity.back_btn

class MypageLeavingActivity : AppCompatActivity() {
	lateinit var reasonList : Array<CheckBox>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_leaving_activity)

		back_btn.setOnClickListener { onBackPressed() }

		reasonList = arrayOf(reason_1, reason_2, reason_3, reason_4, reason_5)

		for (reason in reasonList)
			reason.setOnClickListener(reasonClickListener)

		confirm_btn.setOnClickListener {
			// val intent = Intent(this@MypageLeavingActivity, MyAlertPopup::class.java)
		}
	}

	val reasonClickListener = View.OnClickListener {
		if (countCheckedReasons() > 2) {
			(it as CheckBox).isChecked = false
			Toast.makeText(this@MypageLeavingActivity, "탈퇴 사유는 최대 2개까지 선택할 수 있습니다", Toast.LENGTH_LONG).show()
		}

		if (it.id == R.id.reason_5) {
			reason_else.isEnabled = reason_5.isChecked
		}
	}

	fun countCheckedReasons() : Int {
		var count = 0
		for (reason in reasonList)
			if (reason.isChecked)
				count++
		return count
	}
}
