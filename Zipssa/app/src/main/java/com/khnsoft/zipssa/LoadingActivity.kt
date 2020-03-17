package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class LoadingActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.loading_activity)

		/*
		val identifyIntent = Intent(this@LoadingActivity, AuthWebActivity::class.java)
		startActivityForResult(identifyIntent, JoinActivity.RC_IDENTIFY)
		 */

		val intentService = Intent(this@LoadingActivity, AlarmService::class.java)
		startService(intentService)

		if (UserData.accountType == AccountType.KAKAO || UserData.accountType == AccountType.GOOGLE) {
			val result = ServerHandler.send(this@LoadingActivity, EndOfAPI.USER_GET)

			MyLogger.d("@@@", result.toString())
			if (HttpHelper.isOK(result)) {
				UserData.name = result["data"].asJsonObject["username"].asString
			}

			// TODO("Check offline count")
			val result2 = ServerHandler.send(this@LoadingActivity, EndOfAPI.LOCAL_GET_OFFLINE_COUNT)
			if (HttpHelper.isOK(result2)) {
				val lCounts = result2["data"].asJsonArray
				val lList = JsonArray()
				for (item in lCounts) {
					val jItem = item.asJsonObject
					if (jItem["send_failed"].asInt != 0) {
						val jTemp = JsonObject()
						jTemp.addProperty("alarmId", jItem["taken_alarm_id"].asInt)
						jTemp.addProperty("date", jItem["taken_date"].asString)
						jTemp.addProperty("count", jItem["send_failed"].asInt)
						lList.add(jTemp)
					}
				}

				if (lList.size() > 0) {
					val json = JsonObject()
					json.add("counts", lList)
					val result3 = ServerHandler.send(this@LoadingActivity, EndOfAPI.PUT_COUNT_ALARMS, json)
					if (HttpHelper.isOK(result3)) {
						val result4 = ServerHandler.send(this@LoadingActivity, EndOfAPI.LOCAL_CLEAR_OFFLINE_COUNT)
						if (!HttpHelper.isOK(result4))
							MyLogger.e("MedicineTaken", "Can not delete already sent offline data")
					}
				}
			}
		}

		UserData.version = packageManager.getPackageInfo(packageName, 0).versionName

		val intent = Intent(this@LoadingActivity, MainActivity::class.java)
		startActivity(intent)
		finish()
	}

	// Personal verify
	/*
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == JoinActivity.RC_IDENTIFY) {
			if (data == null) finish()
			MyLogger.d("@@@", "RECEIVED DATA")
			val impUid = data?.getStringExtra(ExtraAttr.IDENTIFY_IMPKEY)
			val sResult = AuthAsyncTask().execute(AuthMethod.GET_TOKEN.name, getString(R.string.imp_key), getString(R.string.imp_secret)).get()
			MyLogger.i("@@@", sResult)
			val result = JsonParser.parseString(sResult).asJsonObject
			if (result["code"].asInt == 0) {
				val accessToken = result["response"].asJsonObject["access_token"].asString
				val result2 = AuthAsyncTask().execute(AuthMethod.CERTIFICATION.name, accessToken, impUid).get()
				MyLogger.i("@@@", result2)
			}

		}
	}
	 */
}
