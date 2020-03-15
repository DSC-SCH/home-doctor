package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LoadingActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.loading_activity)

		if (UserData.accountType == AccountType.KAKAO || UserData.accountType == AccountType.GOOGLE) {
			val result = ServerHandler.send(this@LoadingActivity, EndOfAPI.USER_GET)

			MyLogger.d("@@@", result.toString())
			if (HttpHelper.isOK(result)) {
				UserData.name = result["data"].asJsonObject["username"].asString
			}
		}

		// TODO("Remove above lines")
		NotificationHandler.notify(this@LoadingActivity)

		UserData.version = packageManager.getPackageInfo(packageName, 0).versionName

		val intent = Intent(this@LoadingActivity, MainActivity::class.java)
		startActivity(intent)
		finish()
	}
}
