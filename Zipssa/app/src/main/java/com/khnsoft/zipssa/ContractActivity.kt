package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.account_contract_activity.*

class ContractActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.account_contract_activity)

		val num = intent.getIntExtra("num", -1)

		// TODO("Receive and show contract content")

		contract_confirm_btn.setOnClickListener {
			val result = Intent()
			setResult(num, result)
			finish()
		}
	}
}
