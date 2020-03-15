package com.khnsoft.zipssa

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
					alertTitle = "문의"
					alertContent = "해당 내용으로 문의하시겠습니까?"
					alertConfirmText = "전송"
					confirmListener = View.OnClickListener {
						val json = JsonObject()
						json.addProperty("content", enquire_content.text.toString())
						json.addProperty("email", email_input.text.toString())

						val result = ServerHandler.send(this@MypageServiceEnquireActivity, EndOfAPI.ENQUIRE, json)
						if (!HttpHelper.isOK(result)) return@OnClickListener

						val data = MyAlertPopup.Data(AlertType.CONFIRM)
						data.alertTitle = alertTitle
						data.alertContent = "문의 내용이 전달되었습니다."
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
				Toast.makeText(this@MypageServiceEnquireActivity, "회신 이메일 주소를 확인해 주세요", Toast.LENGTH_LONG).show()
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
