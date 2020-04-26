package com.designvalue.medihand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.mypage_service_activity.*

class MypageServiceActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_service_activity)

		back_btn.setOnClickListener { onBackPressed() }

		contract_btn.setOnClickListener(btnClickListener)
		enquire_btn.setOnClickListener(btnClickListener)
	}

	val btnClickListener = View.OnClickListener {
		val intent = Intent(this@MypageServiceActivity, when (it.id) {
			R.id.contract_btn -> MypageServiceContractActivity::class.java
			R.id.enquire_btn -> MypageServiceEnquireActivity::class.java
			else -> MypageServiceActivity::class.java
		})

		startActivity(intent)
	}
}
