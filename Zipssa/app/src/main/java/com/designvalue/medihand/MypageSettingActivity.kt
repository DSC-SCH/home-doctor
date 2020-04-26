package com.designvalue.medihand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.mypage_setting_activity.*

class MypageSettingActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_setting_activity)

		back_btn.setOnClickListener { onBackPressed() }

		val sp = SPHandler.getSp(this@MypageSettingActivity)
		val editor = sp.edit()

		setting_notify.isChecked = sp.getBoolean(AlarmReceiver.SP_DO_NOTIFY, true)

		setting_notify.setOnCheckedChangeListener { compoundButton, checked ->
			editor.putBoolean(AlarmReceiver.SP_DO_NOTIFY, checked)
			editor.apply()
		}
	}
}
