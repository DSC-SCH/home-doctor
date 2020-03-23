package com.designvalue.medihand

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MyAlertPopup: AppCompatActivity() {

	companion object {
		const val RC = 100

		fun needOnline(context: Context?) {
			val data = Data(AlertType.CONFIRM)
			data.alertTitle = "서비스 제한"
			data.alertContent = "로그인이 필요한 서비스입니다."
			val dataId = DataPasser.insert(data)

			val intent = Intent(context, MyAlertPopup::class.java)
			intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
			context?.startActivity(intent)
		}

		fun needNetwork(context: Context?) {
			val data = Data(AlertType.CONFIRM)
			data.alertTitle = "서비스 제한"
			data.alertContent = "인터넷이 연결되어야 수정이 가능합니다."
			val dataId = DataPasser.insert(data)

			val intent = Intent(context, MyAlertPopup::class.java)
			intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
			context?.startActivity(intent)
		}
	}

	class Data(var type: AlertType) {
		var alertTitle: String = ""
		var alertContent: String = ""
		var alertConfirmText: String = ""
		var confirmListener : View.OnClickListener = View.OnClickListener { }
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val data_id = intent.getIntExtra(ExtraAttr.POPUP_DATA, -1)
		val data = DataPasser.pop(data_id)
		if (data == null) finish()

		val type = data!!.type

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
				resultIntent.putExtra(ExtraAttr.POPUP_RESULT, StatusCode.SUCCESS.status)
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