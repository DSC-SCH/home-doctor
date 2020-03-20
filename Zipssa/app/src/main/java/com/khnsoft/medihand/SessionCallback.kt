package com.khnsoft.medihand

import android.content.Intent
import android.widget.Toast
import com.google.gson.JsonObject
import com.kakao.auth.ISessionCallback
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.usermgmt.response.model.Gender
import com.kakao.util.exception.KakaoException

class SessionCallback(val context: LoginActivity) : ISessionCallback {
	override fun onSessionOpenFailed(exception: KakaoException?) {
		exception?.printStackTrace()
	}

	override fun onSessionOpened() {
		requestMe()
	}

	fun requestMe() {
		UserManagement.getInstance().me(object: MeV2ResponseCallback() {
			override fun onSuccess(result: MeV2Response?) {
				val kakaoAccount = result?.kakaoAccount

				if (kakaoAccount != null) {
					context.loadingOn()
					val nickname = kakaoAccount.profile.nickname
					val email = kakaoAccount.email
					val gender = kakaoAccount.gender

					val json = JsonObject()
					json.addProperty("snsType", AccountType.KAKAO.name)
					json.addProperty("snsId", result.id.toString())
					val loginResult = ServerHandler.send(null, EndOfAPI.USER_LOGIN, json)

					val sp = SPHandler.getSp(context)
					val editor = sp.edit()

					when (loginResult["status"].asInt) {
						HttpHelper.OK_CODE -> {
							val userData = loginResult["data"].asJsonObject
							UserData.accountType = AccountType.KAKAO
							UserData.token = userData["token"].asString
							UserData.id = userData["userId"].asInt
							UserData.name = userData["username"].asString

							editor.putString(AlarmReceiver.SP_TOKEN, UserData.token)
							editor.apply()
							if (sp.getInt(LoginActivity.SP_USER_ID, UserData.DEFAULT_ID) != UserData.id) {
								editor.putInt(LoginActivity.SP_USER_ID, UserData.id)
								editor.putString(LoginActivity.SP_USER_NAME, UserData.name)
								editor.apply()
							}
							if (sp.getString(LoginActivity.SP_LOGIN, UserData.DEFAULT_ACCOUNT.name) != AccountType.KAKAO.name) {
								editor.putString(LoginActivity.SP_LOGIN, AccountType.KAKAO.name)
								editor.apply()
							}

							context.startLoading()
						}
						HttpHelper.NO_USER_CODE -> {
							val intent = Intent(context, JoinActivity::class.java)
							intent.putExtra("sns_type", AccountType.KAKAO.name)
							intent.putExtra("sns_id", result.id.toString())
							intent.putExtra("name", nickname)
							intent.putExtra("email", email)
							intent.putExtra("gender", when(gender) {
								Gender.MALE -> com.khnsoft.medihand.Gender.MEN.name
								Gender.FEMALE -> com.khnsoft.medihand.Gender.WOMEN.name
								else -> null
							})
							context.startActivity(intent)
							context.loadingOff()
						}
						else -> {
							if ((sp.getString(AlarmReceiver.SP_TOKEN, "")?:"").isBlank()) {
								Toast.makeText(context, "최소 한 번은 인터넷이 연결된 환경에서 실행해야 합니다", Toast.LENGTH_SHORT).show()
								context.loadingOff()
								return
							}
							UserData.accountType = AccountType.NO_NETWORK
							context.startLoading()
						}
					}
				}
			}

			override fun onFailure(errorResult: ErrorResult?) {
				super.onFailure(errorResult)
				MyLogger.e("Kakao Login", errorResult.toString())
				context.loadingOff()
			}

			override fun onSessionClosed(errorResult: ErrorResult?) {
				MyLogger.e("Kakao Login", "onSessionClosed: ${errorResult?.errorMessage ?: "null"}")
				context.loadingOff()
			}
		})
	}
}