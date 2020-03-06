package com.khnsoft.zipssa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.mypage_changepw_activity.*

class MypageChangepwActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_changepw_activity)

		back_btn.setOnClickListener {
			onBackPressed()
		}

		// TODO("Change passwd")
	}
}
