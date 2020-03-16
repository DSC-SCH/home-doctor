package com.khnsoft.zipssa

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kakao.usermgmt.response.model.UserAccount
import kotlinx.android.synthetic.main.mypage_activity.*
import java.sql.Time

class MypageActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_activity)

		UserData.careUser = null

		back_btn.setOnClickListener {
			onBackPressed()
		}

		if (UserData.accountType == AccountType.KAKAO || UserData.accountType == AccountType.GOOGLE) {
			account_text.text = "로그아웃"
			account_btn.setOnClickListener {
				val data = MyAlertPopup.Data(AlertType.ALERT).apply {
					alertTitle = "로그아웃"
					alertContent = "로그아웃 하시겠습니까? 어플이 종료됩니다."
					alertConfirmText = "확인"
					confirmListener = View.OnClickListener {
						val sp = SPHandler.getSp(this@MypageActivity)
						val editor = sp.edit()
						editor.putString(LoginActivity.SP_LOGIN, AccountType.NO_LOGIN.type)
						editor.apply()

						val handler = Handler()
						handler.postDelayed({
							finishAffinity()
							System.runFinalization()
							System.exit(0)
						}, 500)
					}
				}

				val dataId = DataPasser.insert(data)
				val intent = Intent(this@MypageActivity, MyAlertPopup::class.java)
				intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
				startActivityForResult(intent, MyAlertPopup.RC)
			}
		} else {
			// TODO("Offline user sign in")
		}

		myprofile_btn.setOnClickListener(btnClickListener)
		notice_btn.setOnClickListener(btnClickListener)
		sync_btn.setOnClickListener(btnClickListener)
		history_btn.setOnClickListener(btnClickListener)
		label_btn.setOnClickListener(btnClickListener)
		setting_btn.setOnClickListener(btnClickListener)
		leave_btn.setOnClickListener(btnClickListener)
		service_btn.setOnClickListener(btnClickListener)
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
				else -> MypageActivity::class.java
			}
		)

		startActivity(intent)
	}
}