package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.permission_activity.*

class PermissionActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.permission_activity)

		// TODO("Remove above lines")
		val intent = Intent(this@PermissionActivity, LoginActivity::class.java)
		startActivity(intent)
		finish()

		// TODO("Request required permissions")

		permission_confirm_btn.setOnClickListener {
			val intent = Intent(this@PermissionActivity, LoginActivity::class.java)
			startActivity(intent)
			finish()
		}
	}
}
