package com.khnsoft.medihand

import com.kakao.auth.*
import com.khnsoft.medihand.GlobalApplication

class KakaoSDKAdapter : KakaoAdapter() {
	override fun getApplicationConfig(): IApplicationConfig {
		return IApplicationConfig {
			GlobalApplication.getGlobalApplicationContext()
		}
	}

	override fun getSessionConfig(): ISessionConfig {
		return object: ISessionConfig {
			override fun isSaveFormData(): Boolean {
				return true
			}

			override fun getAuthTypes(): Array<AuthType> {
				return arrayOf(AuthType.KAKAO_ACCOUNT)
			}

			override fun isSecureMode(): Boolean {
				return false
			}

			override fun getApprovalType(): ApprovalType? {
				return ApprovalType.INDIVIDUAL
			}

			override fun isUsingWebviewTimer(): Boolean {
				return false
			}
		}
	}
}