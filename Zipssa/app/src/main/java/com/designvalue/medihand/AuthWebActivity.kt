package com.designvalue.medihand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.auth_activity.*

class AuthWebActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.auth_activity)

		auth_web.loadUrl("file///android_asset/www/identify.html")
		auth_web.addJavascriptInterface(SmsAuthFactory(this@AuthWebActivity), "AndroidBridge")
	}
}