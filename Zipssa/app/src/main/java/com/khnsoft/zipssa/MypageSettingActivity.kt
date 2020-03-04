package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.mypage_setting_activity.*

class MypageSettingActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_setting_activity)

		back_btn.setOnClickListener { onBackPressed() }
	}
}
