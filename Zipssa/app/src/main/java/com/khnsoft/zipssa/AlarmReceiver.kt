package com.khnsoft.zipssa

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
	companion object {
		const val TAG = "AlarmReceiver"
		const val ACTION_NOTIFY = "action_notify"
		const val ACTION_TAKEN = "action_taken"
		const val ACTION_CLEAR = "action_clear"

		const val DEFAULT_ID = 10000
		const val CHANNEL_ID = DEFAULT_ID.toString()
		const val CHANNEL_NAME = "약손"
		const val IMPORTANCE = NotificationManager.IMPORTANCE_HIGH
		const val SP_LAST_ID = "last_id"
		const val SP_DO_NOTIFY = "do_notify"
		const val SP_SEND_FAILED = "send_failed"

		const val SP_STOP_UNTIL = "stop_until"
		const val SP_STOP_ID = "stop_id"

		fun createNotificationNow(context: Context, notificationManager: NotificationManager, jItem: JsonObject, time: Long) {
			val sp = SPHandler.getSp(context)
			if (!sp.getBoolean(SP_DO_NOTIFY, true)) return

			if (sp.getLong(SP_STOP_UNTIL, -1) > System.currentTimeMillis()) {
				val stopIds = JsonParser.parseString(sp.getString(SP_STOP_ID, "[]")).asJsonArray
				for (stopId in stopIds) {
					if (stopId.asInt == jItem["alarm_id"].asInt) return
				}
			}

			// Generate notification channel if needed
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE)
				notificationManager.createNotificationChannel(channel)
			}

			// Set intent for button
			val takenIntent = Intent(context, AlarmReceiver::class.java)
			takenIntent.setAction(ACTION_TAKEN)
			takenIntent.putExtra(ExtraAttr.NOTIFY_DATA, jItem.toString())
			takenIntent.putExtra(ExtraAttr.NOTIFY_TIME, time)
			val pIntent = PendingIntent.getBroadcast(context, 0, takenIntent, PendingIntent.FLAG_UPDATE_CURRENT)

			// Set notification
			val date = Date(time)
			val builder = NotificationCompat.Builder(context, CHANNEL_ID)
				.setSmallIcon(R.drawable.pill)
				.setContentTitle(SDF.timeInKorean.format(date))
				.setContentText("${jItem["alarm_title"].asString} 복용할 시간이에요!")
				.addAction(R.drawable.confirm_icon, "복용", pIntent)
				.setOngoing(true)

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				builder.setCategory(Notification.CATEGORY_MESSAGE)
					.setPriority(NotificationCompat.PRIORITY_HIGH)
					.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
			}

			val notifyId = DEFAULT_ID + jItem["alarm_id"].asInt
			notificationManager.notify(notifyId, builder.build())
			MyLogger.d("@@@", "NotifyCreated - ${notifyId}")
		}

		fun clearAlarmById(context: Context, alarmManager: AlarmManager, notificationManager: NotificationManager, alarmId: Int) {
			val cancelIntent = Intent(context, AlarmReceiver::class.java)
			val notifyId = DEFAULT_ID + alarmId
			val cancelSender = PendingIntent.getBroadcast(context, notifyId, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT)

			notificationManager.cancel(notifyId)
			if (cancelSender != null) {
				alarmManager.cancel(cancelSender)
				cancelSender.cancel()
			}
			MyLogger.d("@@@", "AlarmCleared - ${notifyId}")
		}

		fun createNextAlarm(context: Context, alarmManager: AlarmManager, jItem: JsonObject) {
			if (AlarmStatus.valueOf(jItem["alarm_enabled"].asString) == AlarmStatus.CANCEL) return
			if (AlarmHandler.getLastAlarmTime(jItem) < System.currentTimeMillis()) return

			val nextTime = AlarmHandler.getNextAlarmTime(jItem, Date().time)
			val notifyIntent = Intent(context, AlarmReceiver::class.java)
			notifyIntent.setAction(ACTION_NOTIFY)
			notifyIntent.putExtra(ExtraAttr.NOTIFY_DATA, jItem.toString())
			notifyIntent.putExtra(ExtraAttr.NOTIFY_TIME, nextTime)
			val notifyId = DEFAULT_ID + jItem["alarm_id"].asInt
			val notifySender = PendingIntent.getBroadcast(context, notifyId, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

			alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTime, notifySender)
			MyLogger.d("@@@", "AlarmCreated - ${notifyId} ${SDF.dateTime.format(Date(nextTime))}")
		}
	}

	override fun onReceive(context: Context?, intent: Intent?) {
		if (context==null || intent == null) return
		val action = intent.action

		when (action) {
			Intent.ACTION_BOOT_COMPLETED -> {
				MyLogger.i(TAG, "Reboot detected")
				val serviceIntent = Intent(context, AlarmService::class.java)
				context.startActivity(serviceIntent)
			}
			ACTION_NOTIFY -> {
				val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
				val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
				val jItem = JsonParser.parseString(intent.getStringExtra(ExtraAttr.NOTIFY_DATA)).asJsonObject
				val time = intent.getLongExtra(ExtraAttr.NOTIFY_TIME, -1)

				val alarmId = jItem["alarm_id"].asInt
				val sp = SPHandler.getSp(context)
				if (sp.getInt(SP_LAST_ID, -1) < alarmId) {
					val editor = sp.edit()
					editor.putInt(SP_LAST_ID, alarmId)
					editor.apply()
				}

				clearAlarmById(context, alarmManager, notificationManager, alarmId)
				createNotificationNow(context, notificationManager, jItem, time)
				createNextAlarm(context, alarmManager, jItem)
			}
			ACTION_TAKEN -> {
				val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
				val jItem = JsonParser.parseString(intent.getStringExtra(ExtraAttr.NOTIFY_DATA)).asJsonObject
				val time = intent.getLongExtra(ExtraAttr.NOTIFY_TIME, -1)

				notificationManager.cancel(DEFAULT_ID + jItem["alarm_id"].asInt)

				val json = JsonObject()
				json.addProperty("date", SDF.dateBar.format(Date(time)))
				json.addProperty("count", 1)

				val result = ServerHandler.send(context, EndOfAPI.PUT_COUNT_ALARM, json, jItem["alarm_id"].asInt)
				if (!HttpHelper.isOK(result)) {
					// TODO("json object로 저장 {id:횟수}")
					val sp = SPHandler.getSp(context)
				}
			}
			ACTION_CLEAR -> {
				val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
				val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
				val sp = SPHandler.getSp(context)
				val lastAlarmId = sp.getInt(SP_LAST_ID, 0)

				for (i in 1..lastAlarmId) {
					clearAlarmById(context, alarmManager, notificationManager, i)
				}
			}
		}
	}
}