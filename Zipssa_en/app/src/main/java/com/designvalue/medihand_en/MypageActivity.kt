package com.designvalue.medihand_en

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.mypage_activity.*

class MypageActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_activity)

		UserData.careUser = null

		back_btn.setOnClickListener {
			onBackPressed()
		}

		// TODO("최초 사용 시 사용 설명으로 넘어가기")

		account_text.text = if (UserData.accountType == AccountType.OFFLINE) "Sign in" else "Sign out"
		account_btn.setOnClickListener {
			val data = MyAlertPopup.Data(AlertType.ALERT).apply {
				alertTitle = if (UserData.accountType == AccountType.OFFLINE) "Sign in" else "Sign out"
				alertContent = if (UserData.accountType == AccountType.OFFLINE) "Would you like to sign in? The account after sign in is not synchronized with the offline account" else "Would you like to sign out? The application will be closed."
				alertConfirmText = "Confirm"
				confirmListener = View.OnClickListener {
					AlarmHandler.clearAllAlarms(this@MypageActivity)
					val sp = SPHandler.getSp(this@MypageActivity)
					val editor = sp.edit()
					editor.putString(LoginActivity.SP_LOGIN, AccountType.NO_LOGIN.name)
					editor.putString(AlarmReceiver.SP_SEND_FAILED, "{}")
					editor.apply()

					val handler = Handler()
					handler.postDelayed({
						finishAffinity()
						System.runFinalization()
						System.exit(0)
					}, 1000)
				}
			}

			val dataId = DataPasser.insert(data)
			val intent = Intent(this@MypageActivity, MyAlertPopup::class.java)
			intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
			startActivityForResult(intent, MyAlertPopup.RC)
		}

		myprofile_btn.setOnClickListener(btnClickListener)
		notice_btn.setOnClickListener(btnClickListener)
		sync_btn.setOnClickListener(btnClickListener)
		history_btn.setOnClickListener(btnClickListener)
		label_btn.setOnClickListener(btnClickListener)
		setting_btn.setOnClickListener(btnClickListener)
		leave_btn.setOnClickListener(btnClickListener)
		service_btn.setOnClickListener(btnClickListener)
		help_btn.setOnClickListener(btnClickListener)
	}

	val btnClickListener = View.OnClickListener {
		when (it.id) {
			R.id.myprofile_btn, R.id.sync_btn -> {
				if (!UserData.isOnline()) {
					MyAlertPopup.needOnline(this@MypageActivity)
					return@OnClickListener
				}
			}
		}

		val intent = Intent(
			this@MypageActivity, when (it.id) {
				R.id.myprofile_btn -> MypageMyprofileActivity::class.java
				R.id.notice_btn -> MypageNoticeActivity::class.java
				R.id.sync_btn -> MypageSyncActivity::class.java
				R.id.history_btn -> MypageHistoryActivity::class.java
				R.id.label_btn -> MypageLabelActivity::class.java
				R.id.setting_btn -> MypageSettingActivity::class.java
				R.id.leave_btn -> MypageLeavingActivity::class.java
				R.id.service_btn -> MypageServiceActivity::class.java
				R.id.help_btn -> MypageHelpActivity::class.java
				else -> MypageActivity::class.java
			}
		)

		startActivity(intent)
	}
}