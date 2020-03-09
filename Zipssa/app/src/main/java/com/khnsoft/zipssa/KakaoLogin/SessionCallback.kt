package com.khnsoft.zipssa.KakaoLogin

import android.util.Log
import com.kakao.auth.ISessionCallback
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.khnsoft.zipssa.AccountType
import com.khnsoft.zipssa.ServerHandler

class SessionCallback : ISessionCallback {
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
					val nickname = kakaoAccount.profile.nickname
					val email = kakaoAccount.email
					val birthday = kakaoAccount.birthday
					val gender = kakaoAccount.gender

					if (ServerHandler.isUserExists(AccountType.KAKAO, "${result.id}")) {
						// TODO("Login")
					} else {
						// TODO("Register")
					}

					Log.i("@@@", "${nickname}, ${email}, ${birthday}, ${gender}")
				}
			}

			override fun onSessionClosed(errorResult: ErrorResult?) {
				Log.e("SessionCallback", "onSessionClosed: ${errorResult?.errorMessage ?: "null"}")
			}
		})
	}
}