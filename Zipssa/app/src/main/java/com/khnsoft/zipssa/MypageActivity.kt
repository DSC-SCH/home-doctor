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

		myprofile_btn.setOnClickListener(btnClickListener)
		notice_btn.setOnClickListener(btnClickListener)
		sync_btn.setOnClickListener(btnClickListener)
		history_btn.setOnClickListener(btnClickListener)
		setting_btn.setOnClickListener(btnClickListener)
	}

	val btnClickListener = View.OnClickListener {
		val intent = Intent(this@MypageActivity, when(it.id) {
			R.id.myprofile_btn -> MypageMyprofileActivity::class.java
			R.id.notice_btn -> MypageNoticeActivity::class.java
			R.id.sync_btn -> MypageSyncActivity::class.java
			R.id.history_btn -> MypageHistoryActivity::class.java
			R.id.setting_btn -> MypageSettingActivity::class.java
			else -> MypageActivity::class.java
		})

		startActivity(intent)
	}
}