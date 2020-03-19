package com.khnsoft.medihand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.mypage_notice_detail_activity.*
import kotlinx.android.synthetic.main.mypage_service_contract_detail_activity.back_btn

class MypageNoticeDetailActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_notice_detail_activity)

		back_btn.setOnClickListener { onBackPressed() }

		val jNotice = JsonParser.parseString(intent.getStringExtra(ExtraAttr.NOTICE)).asJsonObject

		notice_title.text = jNotice["title"].asString
		notice_content.text = jNotice["content"].asString
		notice_date.text = SDF.dateDot.format(SDF.dateBar.parse(jNotice["createdDate"].asString))
	}
}