package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.mypage_myprofile_activity.*

class MypageMyprofileActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_myprofile_activity)

		back_btn.setOnClickListener { onBackPressed() }

		// TODO("Show profile")
	}
}
