package com.khnsoft.zipssa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.mypage_notice_detail_activity.*

class MypageNoticeDetailActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_notice_detail_activity)

		val jNotice = JsonParser.parseString(intent.getStringExtra("data")).asJsonObject
		// TODO("Show notice data")
	}
}