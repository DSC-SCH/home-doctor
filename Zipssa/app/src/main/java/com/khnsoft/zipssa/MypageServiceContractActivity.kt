package com.khnsoft.zipssa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.mypage_service_contract_activity.*

class MypageServiceContractActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_service_contract_activity)

		back_btn.setOnClickListener {
			onBackPressed()
		}
	}
}
