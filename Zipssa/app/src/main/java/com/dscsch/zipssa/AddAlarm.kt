package com.dscsch.zipssa

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.add_alarm_activity.*
import java.text.SimpleDateFormat
import java.util.*

class AddAlarm : AppCompatActivity() {
	val sdf_time = SimpleDateFormat("a hh:mm", Locale.KOREAN)
	val sdf_date = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN)
	val sdf_time_save = SimpleDateFormat("HH:mm", Locale.KOREAN)
	val sdf_date_save = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.add_alarm_activity)

		setSupportActionBar(add_toolbar)

		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)

		// Setting alarm count
		val alarm_counts_array = resources.getStringArray(R.array.alarm_times)
		val adapter = ArrayAdapter<String>(applicationContext, R.layout.alarm_count_spinner_item, alarm_counts_array)
		adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
		alarm_times.adapter = adapter

		alarm_times.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onNothingSelected(parent: AdapterView<*>?) {

			}

			override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
				add_time_container.removeAllViews()
				val count: Int
				when (position) {
					1 -> count = 1
					2 -> count = 2
					3 -> count = 3
					4 -> count = 6
					5 -> count = 8
					6 -> count = 12
					else -> count = 0
				}
				add_times_switch.tag = count

				for (i in 1..count) {
					val item = AddTimeItem(this@AddAlarm)
					add_time_container.addView(item)
					val time = findViewById<TextView>(R.id.time_item)
					val id = resources.getIdentifier("time_item_" + i, "id", applicationContext.packageName)
					time.id = id
					time.text = "오전 09:00"
					time.setOnClickListener {
						val timeView = it as TextView
						val cal = Calendar.getInstance()
						cal.time = sdf_time.parse(timeView.text.toString())
						TimePickerDialog(this@AddAlarm, { timePicker: TimePicker, hourOfDay: Int, minute: Int ->
							cal[Calendar.HOUR_OF_DAY] = hourOfDay
							cal[Calendar.MINUTE] = minute
							timeView.text = sdf_time.format(cal.time)
						}, cal[Calendar.HOUR_OF_DAY], cal[Calendar.MINUTE], false).show()
					}
				}
			}
		}

		// Setting start and end date
		val startCal = Calendar.getInstance()
		val endCal = Calendar.getInstance()
		endCal.add(Calendar.DAY_OF_MONTH, 6)
		add_start_date.text =sdf_date.format(startCal.time)
		add_end_date.text =sdf_date.format(endCal.time)

		add_start_date.setOnClickListener {
			DatePickerDialog(this@AddAlarm, {view, year, month, dayOfMonth ->
				startCal[Calendar.YEAR] = year
				startCal[Calendar.MONTH] = month
				startCal[Calendar.DAY_OF_MONTH] = dayOfMonth
				add_start_date.text = sdf_date.format(startCal.time)
			}, startCal[Calendar.YEAR], startCal[Calendar.MONTH], startCal[Calendar.DAY_OF_MONTH]).show()
		}

		add_end_date.setOnClickListener {
			DatePickerDialog(this@AddAlarm, {view, year, month, dayOfMonth ->
				endCal[Calendar.YEAR] = year
				endCal[Calendar.MONTH] = month
				endCal[Calendar.DAY_OF_MONTH] = dayOfMonth
				add_end_date.text = sdf_date.format(endCal.time)
			}, endCal[Calendar.YEAR], endCal[Calendar.MONTH], endCal[Calendar.DAY_OF_MONTH]).show()
		}

		// Setting repeats
		add_repeat_group.setOnCheckedChangeListener { radioGroup: RadioGroup, res_id: Int ->
			when(res_id) {
				R.id.add_repeat_everyday -> {
					add_date_sun.isEnabled = false
					add_date_mon.isEnabled = false
					add_date_tue.isEnabled = false
					add_date_wed.isEnabled = false
					add_date_thu.isEnabled = false
					add_date_fri.isEnabled = false
					add_date_sat.isEnabled = false

					add_date_sun.isChecked = true
					add_date_mon.isChecked = true
					add_date_tue.isChecked = true
					add_date_wed.isChecked = true
					add_date_thu.isChecked = true
					add_date_fri.isChecked = true
					add_date_sat.isChecked = true
				}
				else -> {
					add_date_sun.isEnabled = true
					add_date_mon.isEnabled = true
					add_date_tue.isEnabled = true
					add_date_wed.isEnabled = true
					add_date_thu.isEnabled = true
					add_date_fri.isEnabled = true
					add_date_sat.isEnabled = true
				}
			}
		}

		// Setting submit the input
		submit_button.setOnClickListener {
			val count = add_times_switch.tag as Int

			val lTimes = mutableListOf<String>()
			for (i in 1..count) {
				val id = resources.getIdentifier("time_item_" + i, "id", applicationContext.packageName)
				lTimes.add("\"${sdf_time_save.format(sdf_time.parse(findViewById<TextView>(id).text.toString()))}\"")
			}
			lTimes.sort()

			val lRepeats = mutableListOf<Int>()
			if (add_date_sun.isChecked) lRepeats.add(0)
			if (add_date_mon.isChecked) lRepeats.add(1)
			if (add_date_tue.isChecked) lRepeats.add(2)
			if (add_date_wed.isChecked) lRepeats.add(3)
			if (add_date_thu.isChecked) lRepeats.add(4)
			if (add_date_fri.isChecked) lRepeats.add(5)
			if (add_date_sat.isChecked) lRepeats.add(6)

			val sql = """
				|INSERT INTO ALARMS (TITLE, START_DT, END_DT, TIMES, REPEATS, ALARM)
				|VALUES (
				|"${add_title.text}",
				|${sdf_date_save.format(startCal.time)},
				|${sdf_date_save.format(endCal.time)},
				|${if (lTimes.size > 0) "'{${lTimes.joinToString(",")}}'" else "NULL"},
				|${if (lRepeats.size > 0) "'{${lRepeats.joinToString(",")}}'" else "NULL"},
				|${if (add_times_switch.isChecked) 1 else 0}
				|)
			""".trimMargin()
			SharedDB.helper.insertAlarm(SharedDB.database, sql)
		}
	}

	// Setting back button on title bar
	override fun onSupportNavigateUp(): Boolean {
		onBackPressed()
		return true
	}
}
