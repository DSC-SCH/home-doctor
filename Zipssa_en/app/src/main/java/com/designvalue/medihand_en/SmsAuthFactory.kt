package com.designvalue.medihand_en

import android.app.Activity
import android.content.Intent

class SmsAuthFactory(val activity: Activity) {

	@android.webkit.JavascriptInterface
	fun resultAuth(message: Int, impKey: String?) {
		val intent = Intent()

		if (message == StatusCode.SUCCESS.status && impKey != null) {
			intent.putExtra(ExtraAttr.IDENTIFY_RESULT, StatusCode.SUCCESS)
			intent.putExtra(ExtraAttr.IDENTIFY_IMPKEY, impKey)
			activity.setResult(JoinActivity.RC_IDENTIFY, intent)
		} else {
			intent.putExtra(ExtraAttr.IDENTIFY_RESULT, StatusCode.FAILED)
			activity.setResult(JoinActivity.RC_IDENTIFY, intent)
		}

		MyLogger.d("@@@", "WebView ${message}, ${impKey}")
	}
}