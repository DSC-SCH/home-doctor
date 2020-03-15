package com.khnsoft.zipssa

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHandler {
	companion object {
		const val NOTIFY_ID = "10001"
		const val NOTIFY_NAME = "약손"
		const val IMPORTANCE = NotificationManager.IMPORTANCE_HIGH

		fun notify(context: Context) {
			val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				val channel = NotificationChannel(NOTIFY_ID, NOTIFY_NAME, IMPORTANCE)
				notificationManager.createNotificationChannel(channel)
			}

			val builder = NotificationCompat.Builder(context, NOTIFY_ID)
				.setContentTitle(NOTIFY_NAME)
				.setContentText("잠시만 기다려주세요")
				.setSmallIcon(R.drawable.ic_home_24px)

			notificationManager.notify(0, builder.build())
		}
	}
}