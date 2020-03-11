package com.khnsoft.zipssa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MyAlertPopup: AppCompatActivity() {

	companion object {
		const val EXTRA_DATA = "data_id"
		const val EXTRA_RESULT = "result"
		const val RC = 100
	}

	class Data(var type: AlertType) {
		var alertTitle: String = ""
		var alertContent: String = ""
		var alertConfirmText: String = ""
		var confirmListener : View.OnClickListener = View.OnClickListener { }
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val data_id = intent.getIntExtra(EXTRA_DATA, -1)
		val data = DataPasser.pop(data_id)
		if (data == null) finish()

		val type = data!!.type

		Log.i("@@@", "Open")

		if (type == AlertType.CONFIRM) {
			setContentView(R.layout.popup_confirm)
			findViewById<TextView>(R.id.popup_title).text = data.alertTitle
			findViewById<TextView>(R.id.popup_content).text = data.alertContent
			findViewById<TextView>(R.id.popup_confirm).setOnClickListener {
				finish()
			}
		} else if (type == AlertType.ALERT) {
			setContentView(R.layout.popup_alert)
			findViewById<TextView>(R.id.popup_title).text = data.alertTitle
			findViewById<TextView>(R.id.popup_content).text = data.alertContent
			findViewById<TextView>(R.id.popup_confirm).text = data.alertConfirmText
			findViewById<TextView>(R.id.popup_confirm).setOnClickListener {
				data.confirmListener.onClick(it)

				val resultIntent = Intent()
				resultIntent.putExtra(EXTRA_RESULT, StatusCode.SUCCESS.status)
				setResult(RC, resultIntent)

				finish()
			}
			findViewById<TextView>(R.id.popup_cancel).setOnClickListener {
				finish()
			}
		}
		else finish()
	}

	override fun onBackPressed() {
		finish()
	}
}