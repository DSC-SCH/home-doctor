package com.khnsoft.zipssa

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.mypage_sync_addmanager_popup.*

class MypageSyncManagerPopup : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
		super.onCreate(savedInstanceState, persistentState)
		setContentView(R.layout.mypage_sync_addmanager_popup)

		val result = ServerHandler.send(this@MypageSyncManagerPopup, EndOfAPI.SYNC_GENERATE)

		if (HttpHelper.isOK(result)) {
			val syncCode = result["data"].asJsonObject["code"].asString
			validation_code.text = syncCode
		}

		popup_confirm.setOnClickListener {
			finish()
		}
	}
}