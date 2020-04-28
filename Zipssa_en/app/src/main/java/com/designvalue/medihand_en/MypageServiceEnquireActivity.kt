package com.designvalue.medihand_en

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.mypage_service_enquire_activity.*

class MypageServiceEnquireActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_service_enquire_activity)

		back_btn.setOnClickListener { onBackPressed() }

		confirm_btn.setOnClickListener {
			if (Checker.checkEmail(email_input.text.toString())) {
				val data = MyAlertPopup.Data(AlertType.ALERT).apply {
					alertTitle = "Contact"
					alertContent = "Are you sure to contact us for that?"
					alertConfirmText = "Send"
					confirmListener = View.OnClickListener {
						val json = JsonObject()
						json.addProperty("content", enquire_content.text.toString())
						json.addProperty("email", email_input.text.toString())

						val result = ServerHandler.send(this@MypageServiceEnquireActivity, EndOfAPI.ENQUIRE, json)
						if (!HttpHelper.isOK(result)) {
							Toast.makeText(this@MypageServiceEnquireActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
							return@OnClickListener
						}

						val data = MyAlertPopup.Data(AlertType.CONFIRM)
						data.alertTitle = alertTitle
						data.alertContent = "Your inquiry has been delivered."
						val dataId = DataPasser.insert(data)

						val intent = Intent(this@MypageServiceEnquireActivity, MyAlertPopup::class.java)
						intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
						startActivity(intent)
					}
				}

				val dataId = DataPasser.insert(data)
				val intent = Intent(this@MypageServiceEnquireActivity, MyAlertPopup::class.java)
				intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
				startActivityForResult(intent, MyAlertPopup.RC)
			} else {
				Toast.makeText(this@MypageServiceEnquireActivity, "Please check the e-mail address for replying.", Toast.LENGTH_LONG).show()
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == MyAlertPopup.RC) {
			if (data != null && data.getIntExtra(ExtraAttr.POPUP_RESULT, StatusCode.FAILED.status) == StatusCode.SUCCESS.status)
				finish()
		}
	}
}
