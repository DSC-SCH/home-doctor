package com.khnsoft.zipssa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.mypage_myprofile_activity.*
import java.text.SimpleDateFormat
import java.util.*

class MypageMyprofileActivity : AppCompatActivity() {
	val sdf_date_show = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
	val sdf_date_save = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_myprofile_activity)

		back_btn.setOnClickListener { onBackPressed() }

		val result = ServerHandler.send(this@MypageMyprofileActivity, EndOfAPI.USER_GET)

		if (HttpHelper.isOK(result)) {
			val userData = result["data"].asJsonObject
			user_name.text = userData["username"].asString
			user_email.text = userData["email"].asString
			user_phone.text = userData["phoneNum"].asString
			user_gender.text = if (Gender.valueOf(userData["genderType"].asString) == Gender.MEN) "남" else "여"
			user_birthday.text = sdf_date_show.format(sdf_date_save.parse(userData["birthday"].asString))
		}
	}
}
