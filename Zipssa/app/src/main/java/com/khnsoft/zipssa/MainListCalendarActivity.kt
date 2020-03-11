package com.khnsoft.zipssa

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.list_calendar_activity.*
import org.hugoandrade.calendarviewlib.CalendarView
import java.text.SimpleDateFormat
import java.util.*

class MainListCalendarActivity : AppCompatActivity() {
	val sdf_main_date = SimpleDateFormat("yyyy / MM / dd", Locale.KOREA)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.list_calendar_activity)

		back_btn.setOnClickListener { onBackPressed() }

		calendar.setOnItemClickedListener { _, _, selectedDate ->
			calendar.selectedDate = selectedDate
			val resultIntent = Intent()
			resultIntent.putExtra(MainListFragment.EXTRA_DATE, sdf_main_date.format(selectedDate.time))
			setResult(MainListFragment.RC_DATE, resultIntent)
			finish()
		}

		// TODO("Show alarm list")
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
