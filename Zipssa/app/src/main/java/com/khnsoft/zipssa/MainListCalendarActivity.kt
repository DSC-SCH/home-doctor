package com.khnsoft.zipssa

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.list_calendar_activity.*
import org.hugoandrade.calendarviewlib.CalendarView
import java.util.*

class MainListCalendarActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.list_calendar_activity)

		back_btn.setOnClickListener { onBackPressed() }

		calendar.setOnItemClickedListener { _, _, selectedDate ->
			calendar.selectedDate = selectedDate
		}

		/*
		calendar.addCalendarObject(CalendarView.CalendarObject(
			null,
			Calendar.getInstance(),
			"감기약",
			Color.parseColor("#FF0000")
		))
		 */
	}
}
