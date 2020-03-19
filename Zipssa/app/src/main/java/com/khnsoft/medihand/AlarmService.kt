package com.khnsoft.medihand

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

class AlarmService : Service() {
	var alarmReceiver: AlarmReceiver? = null

	override fun onCreate() {
		super.onCreate()
		MyLogger.i("AlarmService", "Service created")
		alarmReceiver = AlarmReceiver()
		val intentFilter = IntentFilter()
		intentFilter.addAction(AlarmReceiver.ACTION_NOTIFY)
		intentFilter.addAction(AlarmReceiver.ACTION_TAKEN)

		registerReceiver(alarmReceiver, intentFilter)
	}

	override fun onBind(intent: Intent?): IBinder? {
		return null
	}

	override fun onDestroy() {
		MyLogger.i("AlarmService", "Service destroyed")
		super.onDestroy()
		unregisterReceiver(alarmReceiver)
	}
}