package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.account_join_activity.*

class JoinActivity : AppCompatActivity() {
	companion object {
		const val CONTRACT_1 = 1
		const val CONTRACT_2 = 2
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.account_join_activity)

		contract1_content.setOnClickListener {
			startContract(CONTRACT_1)
		}
		contract2_content.setOnClickListener {
			startContract(CONTRACT_2)
		}

		join_confirm_btn.setOnClickListener {
			val intent = Intent(this@JoinActivity, MainActivity::class.java)
			startActivity(intent)
			finish()
		}
	}

	fun startContract(num: Int) {
		val intent = Intent(this@JoinActivity, ContractActivity::class.java)
		intent.putExtra("num", num)
		startActivityForResult(intent, num)
	}
}
