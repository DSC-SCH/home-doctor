package com.khnsoft.zipssa

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.medicine_detail_activity.*
import kotlinx.android.synthetic.main.mypage_activity.*

class MypageActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_activity)

		myprofile_btn.setOnClickListener {
			val intent = Intent(this@MypageActivity, MypageMyprofileActivity::class.java)
			startActivity(intent)
		}

		notice_btn.setOnClickListener {
			val intent = Intent(this@MypageActivity, MypageNoticeActivity::class.java)
			startActivity(intent)
		}
	}
}