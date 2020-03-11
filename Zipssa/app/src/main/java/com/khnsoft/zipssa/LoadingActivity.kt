package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LoadingActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.loading_activity)

		val mHandler = DatabaseHandler.open(this@LoadingActivity)
		mHandler.updateTables(UserData.id)
		mHandler.close()

		val intent = Intent(this@LoadingActivity, MainActivity::class.java)
		startActivity(intent)
		finish()
	}
}
