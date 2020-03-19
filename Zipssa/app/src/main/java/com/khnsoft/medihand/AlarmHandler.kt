package com.khnsoft.medihand

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import com.google.gson.JsonObject
import java.util.*

class AlarmHandler {
	companion object {
		const val CODE_EMPTY = -1L
		const val CODE_ENDED = -2L
		const val CODE_NOT_START = -3L

		fun getNextAlarmTime(jItem: JsonObject, curTimeInMillis: Long, nextCal: Calendar = Calendar.getInstance()) : Long {
			val lTimes = AlarmParser.parseTimes(jItem["alarm_times"].asString)
			val lRepeats = AlarmParser.parseRepeats(jItem["alarm_repeats"].asString)
			val startDate = SDF.dateBar.parse(jItem["alarm_start_date"].asString)
			val endDate = SDF.dateBar.parse(jItem["alarm_end_date"].asString)

			nextCal.timeInMillis = curTimeInMillis
			if (lTimes.size() > 0 && lRepeats.size() > 0) {
				var temp = ""
				selectDay@ while (true) {
					for (weekday in lRepeats) {
						if (weekday.asInt == nextCal[Calendar.DAY_OF_WEEK]) {
							for (i in 0..lTimes.size() - 1) {
								temp = lTimes[i].asString
								val timeNext =
									SDF.dateTime.parse("${SDF.dateBar.format(nextCal.time)} ${temp}").time
								if (curTimeInMillis < timeNext && startDate.time < timeNext) {
									break@selectDay
								}
							}
						}
					}
					nextCal.add(Calendar.DAY_OF_MONTH, 1)
				}

				val timeNext = SDF.dateTime.parse("${SDF.dateBar.format(nextCal.time)} ${temp}").time
				if (timeNext < endDate.time + DateUtils.DAY_IN_MILLIS)
					return timeNext
				return CODE_ENDED
			} else return CODE_EMPTY
		}

		fun getPastAlarmTime(jItem: JsonObject, curTimeInMillis: Long, pastCal: Calendar = Calendar.getInstance()) : Long {
			val lTimes = AlarmParser.parseTimes(jItem["alarm_times"].asString)
			val lRepeats = AlarmParser.parseRepeats(jItem["alarm_repeats"].asString)
			val startDate = SDF.dateBar.parse(jItem["alarm_start_date"].asString)
			val endDate = SDF.dateBar.parse(jItem["alarm_end_date"].asString)

			pastCal.timeInMillis = startDate.time
			if (lTimes.size() > 0 && lRepeats.size() > 0) {
				if (SDF.dateTime.parse("${SDF.dateBar.format(startDate)} ${lTimes[0].asString}").time > curTimeInMillis) return CODE_NOT_START
				var pastDate = SDF.dateBar.format(pastCal.time)
				var pastTime = ""
				var temp = ""
				selectDay@ while (true) {
					for (weekday in lRepeats) {
						if (weekday.asInt == pastCal[Calendar.DAY_OF_WEEK]) {
							for (i in 0..lTimes.size() - 1) {
								temp = lTimes[i].asString
								val timeNext =
									SDF.dateTime.parse("${SDF.dateBar.format(pastCal.time)} ${temp}").time
								if (curTimeInMillis < timeNext && startDate.time < timeNext) {
									break@selectDay
								}
								if (endDate.time + DateUtils.DAY_IN_MILLIS < timeNext) {
									return SDF.dateTime.parse("${pastDate} ${pastTime}").time
								}
								pastDate = SDF.dateBar.format(pastCal.time)
								pastTime = temp
							}
						}
					}
					pastCal.add(Calendar.DAY_OF_MONTH, 1)
				}

				val timeNext = SDF.dateTime.parse("${pastDate} ${pastTime}").time
				return timeNext
			} else return CODE_EMPTY
		}

		fun createAlarm(context: Context?, jItem: JsonObject) {
			val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
			AlarmReceiver.createNextAlarm(context, alarmManager, jItem)
		}

		fun clearAlarmById(context: Context?, alarmId: Int) {
			val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
			AlarmReceiver.clearAlarmById(context, alarmManager, notificationManager, alarmId)
		}

		fun clearAllAlarms(context: Context?) {
			val clearIntent = Intent(context, AlarmReceiver::class.java)
			clearIntent.setAction(AlarmReceiver.ACTION_CLEAR)
			val clearSender = PendingIntent.getBroadcast(context, 0, clearIntent, 0)
			clearSender.send()
		}

		fun getLastAlarmTime(jItem: JsonObject) : Long {
			val lTimes = AlarmParser.parseTimes(jItem["alarm_times"].asString)
			if (lTimes.size() > 0) {
				return SDF.dateTime.parse("${SDF.dateBar.format(SDF.dateBar.parse(jItem["alarm_end_date"].asString))} ${lTimes[lTimes.size()-1].asString}").time
			}
			return -1
		}
	}
}