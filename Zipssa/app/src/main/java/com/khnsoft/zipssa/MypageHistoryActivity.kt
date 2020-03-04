package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.mypage_history_activity.*

class MypageHistoryActivity : AppCompatActivity() {
	val PAGE_MANAGER = 0
	val PAGE_CREW = 1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_history_activity)

		history_all_btn.setOnClickListener {
			val intent = Intent(this@MypageHistoryActivity, MypageHistoryAllActivity::class.java)
			startActivity(intent)
		}

		history_photo_btn.setOnClickListener {
			val intent = Intent(this@MypageHistoryActivity, MypageHistoryPhotoActivity::class.java)
			startActivity(intent)
		}
	}
}
