package com.khnsoft.zipssa

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.mypage_sync_addmanager_popup.*

class MypageSyncManagerPopup : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_sync_addmanager_popup)

		val json = JsonObject()
		json.addProperty("userId", UserData.id)
		val result = ServerHandler.send(this@MypageSyncManagerPopup, EndOfAPI.SYNC_GENERATE, json)

		if (HttpHelper.isOK(result)) {
			val syncCode = result["data"].asJsonObject["code"].asString
			validation_code.text = syncCode
		}

		popup_confirm.setOnClickListener {
			finish()
		}
	}
}