package com.designvalue.medihand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.account_contract_activity.*

class ContractActivity : AppCompatActivity() {

	companion object {
		const val RC_CONTRACT = 100
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.account_contract_activity)

		back_btn.setOnClickListener { onBackPressed() }

		val num = intent.getIntExtra(ExtraAttr.CONTRACT_NUM, -1)
		val jContract = JsonParser.parseString(intent.getStringExtra(ExtraAttr.CONTRACT)).asJsonObject
		val isChecked = intent.getBooleanExtra(ExtraAttr.CONTRACT_CHECKED, false)

		contract_check.text = jContract["title"].asString
		contract_check.isChecked = isChecked
		contract_content.text = jContract["content"].asString

		contract_confirm_btn.setOnClickListener {
			val result = Intent()
			result.putExtra(ExtraAttr.CONTRACT_NUM, num)
			result.putExtra(ExtraAttr.CONTRACT_CHECKED, contract_check.isChecked)
			setResult(RC_CONTRACT, result)
			finish()
		}
	}
}
