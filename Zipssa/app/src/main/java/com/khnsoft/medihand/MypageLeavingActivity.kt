package com.khnsoft.medihand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import kotlinx.android.synthetic.main.mypage_leaving_activity.*
import kotlinx.android.synthetic.main.mypage_setting_activity.back_btn
import java.io.File

class MypageLeavingActivity : AppCompatActivity() {

	val radioGroup = MyRadioGroup()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_leaving_activity)

		back_btn.setOnClickListener { onBackPressed() }

		radioGroup.add(reason_1)
		radioGroup.add(reason_2)
		radioGroup.add(reason_3)
		radioGroup.add(reason_4)
		radioGroup.add(reason_5)

		reason_5.setOnCheckedChangeListener {_, isChecked ->
			reason_else.isEnabled = isChecked
		}

		confirm_btn.setOnClickListener {
			val reason = radioGroup.getCheckedIndex()
			if (reason == -1) {
				Toast.makeText(this@MypageLeavingActivity, "탈퇴 사유를 선택해 주세요", Toast.LENGTH_LONG).show()
				return@setOnClickListener
			} else if (reason == 4 && reason_else.text.toString().isBlank()) {
				Toast.makeText(this@MypageLeavingActivity, "탈퇴 사유를 입력해 주세요", Toast.LENGTH_LONG).show()
				return@setOnClickListener
			}
			val data = MyAlertPopup.Data(AlertType.ALERT).apply {
				alertTitle = "서비스 탈퇴"
				alertContent = "한 번 삭제된 계정은 복구가 불가능합니다."
				alertConfirmText = "탈퇴"
				confirmListener = View.OnClickListener {
					AlarmHandler.clearAllAlarms(this@MypageLeavingActivity)

					val json = JsonObject()
					json.addProperty("content", when (reason) {
						0 -> reason_1.text.toString()
						1 -> reason_2.text.toString()
						2 -> reason_3.text.toString()
						3 -> reason_4.text.toString()
						4 -> reason_else.text.toString()
						else -> {
							Toast.makeText(this@MypageLeavingActivity, "탈퇴 사유를 선택해 주세요", Toast.LENGTH_LONG).show()
							return@OnClickListener
						}
					})

					val result = ServerHandler.send(this@MypageLeavingActivity,EndOfAPI.USER_DELETE, json)
					if (!HttpHelper.isOK(result)) {
						Toast.makeText(this@MypageLeavingActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
						return@OnClickListener
					}

					when (UserData.accountType) {
						AccountType.KAKAO -> {
							UserManagement.getInstance().requestUnlink(object: UnLinkResponseCallback() {
								override fun onSuccess(result: Long?) {
									clearApplicationData()
									killApplication()
								}

								override fun onSessionClosed(errorResult: ErrorResult?) {
									MyLogger.e("KakaoUnlink", errorResult?.errorMessage ?: "ERROR")
								}
							})
						}
						AccountType.GOOGLE -> {
							FirebaseAuth.getInstance().currentUser?.delete()
							clearApplicationData()
							killApplication()
						}
						AccountType.OFFLINE -> {
							clearApplicationData()
							killApplication()
						}
						else -> {
							Toast.makeText(this@MypageLeavingActivity, "온라인 사용자는 인터넷이 연결된 상태에서 탈퇴를 시도해주세요.", Toast.LENGTH_LONG).show()
						}
					}
				}
			}

			val dataId = DataPasser.insert(data)
			val intent = Intent(this@MypageLeavingActivity, MyAlertPopup::class.java)
			intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
			startActivityForResult(intent, MyAlertPopup.RC)
		}
	}

	fun killApplication() {
		finishAffinity()
		System.runFinalization()
		System.exit(0)
	}

	fun deleteCache(dir: File) : Boolean{
		if (dir != null && dir.isDirectory) {
			val children = dir.list()
			for (child in children) {
				val isSuccess = deleteCache(File(dir, child))
				if (!isSuccess) return false
			}
		}
		return dir.delete()
	}

	fun clearApplicationData() {
		val appDir = File(cacheDir.parent)
		if (appDir.exists()) {
			val children = appDir.list()
			for (child in children) {
				if (child != "lib" && child != "files") {
					deleteCache(File(appDir, child))
				}
			}
		}
	}
}
