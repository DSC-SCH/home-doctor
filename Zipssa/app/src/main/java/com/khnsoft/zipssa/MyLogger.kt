package com.khnsoft.zipssa

import android.util.Log

class MyLogger {
	companion object {
		const val MAX_LEN = 3000

		fun i(tag: String, msg: String) {
			log(LogType.INFO, tag, msg)
		}

		fun e(tag: String, msg: String) {
			log(LogType.ERROR, tag, msg)
		}

		fun d(tag: String, msg: String) {
			log(LogType.DEBUG, tag, msg)
		}

		fun log(type: LogType, tag: String, msg: String) {
			val msgLen = msg.length
			var curLen = 0

			while (curLen < msgLen) {
				if (msgLen - curLen > MAX_LEN) when (type) {
					LogType.INFO -> Log.i(tag, msg.substring(curLen, curLen + MAX_LEN))
					LogType.ERROR -> Log.e(tag, msg.substring(curLen, curLen + MAX_LEN))
					LogType.DEBUG -> Log.d(tag, msg.substring(curLen, curLen + MAX_LEN))
				} else when (type) {
					LogType.INFO -> Log.i(tag, msg.substring(curLen))
					LogType.ERROR -> Log.e(tag, msg.substring(curLen))
					LogType.DEBUG -> Log.d(tag, msg.substring(curLen))
				}
				curLen += MAX_LEN
			}
		}
	}
}