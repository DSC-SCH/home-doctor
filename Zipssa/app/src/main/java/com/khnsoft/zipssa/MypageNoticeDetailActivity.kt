package com.khnsoft.zipssa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.mypage_service_contract_detail_activity.*

class MypageNoticeDetailActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_service_contract_detail_activity)

		back_btn.setOnClickListener { onBackPressed() }

		val jContract = JsonParser.parseString(intent.getStringExtra(ExtraAttr.CONTRACT)).asJsonObject

		contract_title.text = jContract["title"].asString
		contract_content.text = jContract["content"].asString
	}
}