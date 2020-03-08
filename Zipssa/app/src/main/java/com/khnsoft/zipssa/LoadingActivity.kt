package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LoadingActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.loading_activity)

		val mHandler = DBHandler.open(this@LoadingActivity)
		mHandler.updateTables(-1)
		mHandler.close()

		val intent = Intent(this@LoadingActivity, MainActivity::class.java)
		startActivity(intent)
	}
}
