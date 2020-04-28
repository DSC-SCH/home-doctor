package com.designvalue.medihand_en

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.mypage_myprofile_activity.*

class MypageMyprofileActivity : AppCompatActivity() {
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
			user_gender.text = if (Gender.valueOf(userData["genderType"].asString) == Gender.MEN) "Male" else "Female"
			user_birthday.text = SDF.dateInKorean.format(SDF.dateBar.parse(userData["birthday"].asString))
		} else {
			Toast.makeText(this@MypageMyprofileActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
			finish()
		}
	}
}
