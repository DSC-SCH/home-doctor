package com.khnsoft.zipssa

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.mypage_sync_addcrew_popup.*

class MypageSyncCrewPopup : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_sync_addcrew_popup)

		popup_cancel.setOnClickListener {
			finish()
		}

		popup_confirm.setOnClickListener {
			val json = JsonObject()
			json.addProperty("manager", UserData.id)
			json.addProperty("code", validation_code_input.text.toString())

			val result = ServerHandler.send(this@MypageSyncCrewPopup, EndOfAPI.SYNC_CONNECT, json)
			if (HttpHelper.isOK(result)) {
				finish()
			}
		}
	}
}