package com.khnsoft.zipssa

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.list_calendar_activity.*
import org.hugoandrade.calendarviewlib.CalendarView
import java.util.*

class MainListCalendarActivity : AppCompatActivity() {
	var monthCreated = MutableList<Int>(0){0}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.list_calendar_activity)

		back_btn.setOnClickListener { onBackPressed() }

		val result =
			if (UserData.careUser == null) ServerHandler.send(this@MainListCalendarActivity, EndOfAPI.GET_ENABLED_ALARMS)
			else ServerHandler.send(this@MainListCalendarActivity, EndOfAPI.SYNC_GET_ALL_ALARMS, id = UserData.careUser)
		if (!HttpHelper.isOK(result)) {
			Toast.makeText(this@MainListCalendarActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
			finish()
		}
		val lAlarms = result["data"].asJsonArray

		val _curDate = intent.getStringExtra(ExtraAttr.CALENDAR_START)
		val curCal = Calendar.getInstance()
		curCal.time = SDF.dateSlash.parse(_curDate)

		calendar.currentDate = curCal

		calendar.setOnItemClickedListener { _, _, selectedDate ->
			calendar.selectedDate = selectedDate
			val resultIntent = Intent()
			resultIntent.putExtra(ExtraAttr.CALENDAR_DATE, SDF.dateSlash.format(selectedDate.time))
			setResult(MainListFragment.RC_DATE, resultIntent)
			finish()
		}

		calendar.setOnMonthChangedListener { month, year ->
			refresh(lAlarms, year, month)
		}

		refresh(lAlarms, curCal[Calendar.YEAR], curCal[Calendar.MONTH])
	}

	fun refresh(lAlarms: JsonArray, year: Int, month: Int) {
		val dateCode = year*100+month
		if (monthCreated.contains(dateCode)) return
		else monthCreated.add(dateCode)
		val _startMonthCal = Calendar.getInstance()
		_startMonthCal.time = SDF.yearMonth.parse("%d-%02d".format(year, month+1))
		val _endMonthCal = Calendar.getInstance()
		_endMonthCal.time = SDF.yearMonth.parse("%d-%02d".format(year, month+2))
		_endMonthCal.add(Calendar.DAY_OF_MONTH, -1)
		val startMonthTime = _startMonthCal.timeInMillis
		val endMonthTime = _endMonthCal.timeInMillis

		for (item in lAlarms) {
			val jItem = ServerHandler.convertKeys(item.asJsonObject, ServerHandler.alarmToLocal)
			var startTime = SDF.dateBar.parse(jItem["alarm_start_date"].asString).time
			var endTime = SDF.dateBar.parse(jItem["alarm_end_date"].asString).time

			if (jItem["alarm_times"].asString.isBlank()) continue

			// Check if the alarm is in the boundary of current month
			if (startTime < startMonthTime) {
				if (endTime < startMonthTime) continue
				else if (endMonthTime < endTime) endTime = endMonthTime
				startTime = startMonthTime
			} else if (startMonthTime <= startTime && startTime <= endMonthTime) {
				if (endMonthTime < endTime) endTime = endMonthTime
			} else {
				continue
			}

			val curCal = Calendar.getInstance()
			curCal.timeInMillis = startTime
			val sRepeats = jItem["alarm_repeats"].asString
			val lRepeats = AlarmParser.parseRepeats(sRepeats)

			date@while (curCal.timeInMillis <= endTime) {
				for (i in 0..lRepeats.size()-1) {
					val weekday = lRepeats[i].asInt
					if (curCal[Calendar.DAY_OF_WEEK] == weekday) {
						val _cal = Calendar.getInstance()
						_cal.time = curCal.time
						calendar.addCalendarObject(CalendarView.CalendarObject(
							null,
							_cal,
							jItem["alarm_title"].asString,
							Color.parseColor(jItem["label_color"].asString)
						))

						val next = (lRepeats[(i + 1) % lRepeats.size()].asInt + 7 - weekday) % 7
						curCal.add(Calendar.DAY_OF_MONTH, if (next == 0) 7 else next)
						continue@date
					}
				}
				curCal.add(Calendar.DAY_OF_MONTH, 1)
			}
		}
	}
}
