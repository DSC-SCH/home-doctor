package com.khnsoft.medihand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import kotlinx.android.synthetic.main.mypage_leaving_activity.*
import kotlinx.android.synthetic.main.mypage_setting_activity.back_btn
import java.io.File

class MypageLeavingActivity : AppCompatActivity() {
	lateinit var reasonList : Array<CheckBox>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_leaving_activity)

		back_btn.setOnClickListener { onBackPressed() }

		reasonList = arrayOf(reason_1, reason_2, reason_3, reason_4, reason_5)

		for (reason in reasonList)
			reason.setOnClickListener(reasonClickListener)

		confirm_btn.setOnClickListener {
			val data = MyAlertPopup.Data(AlertType.ALERT).apply {
				alertTitle = "서비스 탈퇴"
				alertContent = "한 번 삭제된 계정은 복구가 불가능합니다."
				alertConfirmText = "탈퇴"
				confirmListener = View.OnClickListener {
					AlarmHandler.clearAllAlarms(this@MypageLeavingActivity)
					val result = ServerHandler.send(this@MypageLeavingActivity,EndOfAPI.USER_DELETE)
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

	val reasonClickListener = View.OnClickListener {
		if (countCheckedReasons() > 2) {
			(it as CheckBox).isChecked = false
			Toast.makeText(this@MypageLeavingActivity, "탈퇴 사유는 최대 2개까지 선택할 수 있습니다", Toast.LENGTH_LONG).show()
		}

		if (it.id == R.id.reason_5) {
			reason_else.isEnabled = reason_5.isChecked
		}
	}

	fun countCheckedReasons() : Int {
		var count = 0
		for (reason in reasonList)
			if (reason.isChecked)
				count++
		return count
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
				if (!child.equals("lib") && !child.equals("files")) {
					deleteCache(File(appDir, child))
				}
			}
		}
	}
}
