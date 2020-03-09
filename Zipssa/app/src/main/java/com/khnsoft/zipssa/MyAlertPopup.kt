package com.khnsoft.zipssa

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

class MyAlertPopup: AppCompatActivity() {

	companion object {
		val TYPE_CONFIRM = 0
		val TYPE_ALERT = 1
		val RESULT = 100

		class Data(var type: Int): Serializable {
			var alertTitle: String = ""
			var alertContent: String = ""
			var alertConfirm: String = ""
			var alertConfirmBtn: String = ""
			var sql: String = ""
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val data = intent.getSerializableExtra("data") as Data
		val type = data.type

		if (type == TYPE_CONFIRM) {
			setContentView(R.layout.popup_confirm)
			findViewById<TextView>(R.id.popup_title).text = data.alertTitle
			findViewById<TextView>(R.id.popup_content).text = data.alertConfirm
			findViewById<TextView>(R.id.popup_confirm).setOnClickListener {
				finish()
			}
		} else if (type == TYPE_ALERT) {
			setContentView(R.layout.popup_alert)
			findViewById<TextView>(R.id.popup_title).text = data.alertTitle
			findViewById<TextView>(R.id.popup_content).text = data.alertContent
			findViewById<TextView>(R.id.popup_confirm).text = data.alertConfirmBtn
			findViewById<TextView>(R.id.popup_confirm).setOnClickListener{
				val mHandler = DBHandler.open(this@MyAlertPopup)
				if (mHandler.execNonResult(data.sql)) {
					mHandler.close()
					data.type = TYPE_CONFIRM
					val intent = Intent(this@MyAlertPopup, MyAlertPopup::class.java)
					intent.putExtra("data", data)
					startActivity(intent)
					val resultIntent = Intent().apply {
						putExtra("result", 0)
					}
					setResult(RESULT, resultIntent)
					finish()
				}
				mHandler.close()
			}
			findViewById<TextView>(R.id.popup_cancel).setOnClickListener {
				finish()
			}
		}
		else finish()
	}
}