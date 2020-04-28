package com.designvalue.medihand_en

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

		val sp = SPHandler.getSp(this@LoadingActivity)
		val editor = sp.edit()

		if (UserData.accountType == AccountType.KAKAO || UserData.accountType == AccountType.GOOGLE || UserData.accountType == AccountType.OFFLINE) {
			val jFaileds = JsonParser.parseString(sp.getString(AlarmReceiver.SP_SEND_FAILED, "{}")).asJsonObject
			MyLogger.d("@@@", jFaileds.toString())
			if (jFaileds.size() > 0) {
				val lList = JsonArray()
				for (idDate in jFaileds.keySet()) {
					val splited = idDate.split("/")
					val jTemp = JsonObject()
					jTemp.addProperty("alarmId", Integer.parseInt(splited[0]))
					jTemp.addProperty("date", splited[1])
					jTemp.addProperty("count", jFaileds[idDate].asInt)
					lList.add(jTemp)
				}

				val json = JsonObject()
				json.add("counts", lList)
				val result2 = ServerHandler.send(this@LoadingActivity, EndOfAPI.PUT_COUNT_ALARMS, json)
				if (HttpHelper.isOK(result2)) {
					editor.putString(AlarmReceiver.SP_SEND_FAILED, "{}")
					editor.apply()
				} else {
					Toast.makeText(this@LoadingActivity, result2["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
				}
			}
		}

		if (UserData.isOnline()) {
			val json = JsonObject()
			json.addProperty("user_id", UserData.id)
			json.addProperty("user_name", UserData.name)
			val result = ServerHandler.send(this@LoadingActivity, EndOfAPI.LOCAL_USER_SERVER, json)

			if (!HttpHelper.isOK(result)) {
				Toast.makeText(this@LoadingActivity, result["message"].asString, Toast.LENGTH_SHORT).show()
			}

			val result2 = ServerHandler.send(this@LoadingActivity, EndOfAPI.GET_LABELS)
			if (HttpHelper.isOK(result2)) {
				val result4 = ServerHandler.send(this@LoadingActivity, EndOfAPI.LOCAL_LABEL_SERVER, result2)

				if (!HttpHelper.isOK(result4)) {
					Toast.makeText(this@LoadingActivity, result4["message"].asString, Toast.LENGTH_SHORT).show()
				}
			}

			val result3 = ServerHandler.send(this@LoadingActivity, EndOfAPI.GET_ALL_ALARMS)
			if (HttpHelper.isOK(result3)) {
				val result4 = ServerHandler.send(this@LoadingActivity, EndOfAPI.LOCAL_ALARM_SERVER, result3)

				if (!HttpHelper.isOK(result4)) {
					Toast.makeText(this@LoadingActivity, result4["message"].asString, Toast.LENGTH_SHORT).show()
				}
			}
		} else {
			UserData.name = sp.getString(LoginActivity.SP_USER_NAME, "John Doe")!!
		}

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
			MyLogger.d("@@@", sResult)
			val result = JsonParser.parseString(sResult).asJsonObject
			if (result["code"].asInt == 0) {
				val accessToken = result["response"].asJsonObject["access_token"].asString
				val result2 = AuthAsyncTask().execute(AuthMethod.CERTIFICATION.name, accessToken, impUid).get()
				MyLogger.d("@@@", result2)
			}

		}
	}
	 */
}
