package com.designvalue.medihand

import android.app.Application
import com.kakao.auth.KakaoSDK
import java.lang.IllegalStateException

class GlobalApplication : Application() {
	companion object {
		var instance : GlobalApplication? = null

		fun getGlobalApplicationContext() : GlobalApplication? {
			if (instance == null) {
				throw IllegalStateException("This application does not inherit com.kakao.GlobalApplication")
			}
			return instance
		}
	}

	override fun onCreate() {
		super.onCreate()
		instance = this;

		KakaoSDK.init(KakaoSDKAdapter())
	}

	override fun onTerminate() {
		super.onTerminate()
		instance = null
	}
}