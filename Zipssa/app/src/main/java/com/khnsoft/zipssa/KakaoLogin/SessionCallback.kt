package com.khnsoft.zipssa.KakaoLogin

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.JsonObject
import com.kakao.auth.ISessionCallback
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.usermgmt.response.model.Gender
import com.kakao.util.exception.KakaoException
import com.khnsoft.zipssa.*

class SessionCallback(val context: LoginActivity) : ISessionCallback {
	override fun onSessionOpenFailed(exception: KakaoException?) {
		exception?.printStackTrace()
	}

	override fun onSessionOpened() {
		requestMe()
	}

	fun requestMe() {
		Log.i("@@@", "REQUESTME")
		UserManagement.getInstance().me(object: MeV2ResponseCallback() {
			override fun onSuccess(result: MeV2Response?) {
				val kakaoAccount = result?.kakaoAccount

				if (kakaoAccount != null) {
					val nickname = kakaoAccount.profile.nickname
					val email = kakaoAccount.email
					val gender = kakaoAccount.gender

					Log.i("@@@", result.id.toString())

					val json = JsonObject()
					json.addProperty("snsType", AccountType.KAKAO.type)
					json.addProperty("snsId", result.id.toString())
					val loginResult = ServerHandler.send(null, EndOfAPI.USER_LOGIN, json)

					when (loginResult["status"].asInt) {
						HttpHelper.OK_CODE -> {
							val userData = loginResult["data"].asJsonObject
							UserData.accountType = AccountType.KAKAO
							UserData.token = userData["token"].asString
							UserData.id = userData["userId"].asInt

							val sp = context.getSharedPreferences(SharedPreferencesSrc.SP_NAME, Context.MODE_PRIVATE)
							if (sp.getInt(LoginActivity.SP_USER_ID, UserData.DEFAULT_ID) != UserData.id) {
								val editor = sp.edit()
								editor.putInt(LoginActivity.SP_USER_ID, UserData.id)
								editor.apply()
							}

							context.startLoading()
						}
						HttpHelper.NO_USER_CODE -> {
							val intent = Intent(context, JoinActivity::class.java)
							intent.putExtra("sns_type", AccountType.KAKAO.type)
							intent.putExtra("sns_id", result.id.toString())
							intent.putExtra("name", nickname)
							intent.putExtra("email", email)
							intent.putExtra("gender", when(gender) {
								Gender.MALE -> com.khnsoft.zipssa.Gender.MEN.gender
								Gender.FEMALE -> com.khnsoft.zipssa.Gender.WOMEN.gender
								else -> null
							})
							context.startActivity(intent)
							context.finish()
						}
						else -> {
							UserData.accountType = AccountType.NO_NETWORK
							context.startLoading()
						}
					}
				}
			}

			override fun onFailure(errorResult: ErrorResult?) {
				super.onFailure(errorResult)
				Log.i("@@@", errorResult.toString())
			}

			override fun onSessionClosed(errorResult: ErrorResult?) {
				Log.e("SessionCallback", "onSessionClosed: ${errorResult?.errorMessage ?: "null"}")
			}
		})
	}
}