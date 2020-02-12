package com.khnsoft.zipssa

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.add_alarm_activity.*
import java.text.SimpleDateFormat
import java.util.*

class AddAlarm : AppCompatActivity() {
	val sdf_time_show = SimpleDateFormat("a hh:mm", Locale.KOREA)
	val sdf_date_show = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
	val sdf_time_save = SimpleDateFormat("HH:mm", Locale.KOREA)
	val sdf_date_save = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

	var repeatCount = 7

	val DEFAULT_TIMES = JsonParser.parseString(
		"""[
		|[],
		|["오전 9:00"],
		|["오전 9:00", "오후 7:00"],
		|["오전 9:00", "오후 1:00", "오후 7:00"],
		|["오전 9:00", "오전 11:00", "오후 1:00", "오후 3:00", "오후 5:00", "오후 7:00"],
		|["오전 9:00", "오전 11:00", "오후 1:00", "오후 3:00", "오후 5:00", "오후 7:00", "오후 9:00", "오후 9:00"],
		|["오전 9:00", "오전 11:00", "오후 1:00", "오후 3:00", "오후 5:00", "오후 7:00", "오후 9:00", "오후 9:00", "오후 9:00", "오후 9:00", "오후 9:00", "오후 9:00"]
	]""".trimMargin()
	).asJsonArray

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.add_alarm_activity)

		// Setting back button
		back_btn.setOnClickListener {
			onBackPressed()
		}

		// Setting alarm count
		val alarm_counts_array = resources.getStringArray(R.array.alarm_times)
		val adapter = ArrayAdapter<String>(applicationContext, R.layout.alarm_count_spinner_item, alarm_counts_array)
		adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
		alarm_times.adapter = adapter

		alarm_times.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onNothingSelected(parent: AdapterView<*>?) {

			}

			override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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

				val adapter = AddTimeRecyclerAdapter(this@AddAlarm, DEFAULT_TIMES[position].asJsonArray)
				val lm = LinearLayoutManager(this@AddAlarm)
				add_time_container.layoutManager = lm
				add_time_container.adapter = adapter
			}
		}

		// Setting start and end date
		val startCal = Calendar.getInstance()
		val endCal = Calendar.getInstance()
		endCal.add(Calendar.DAY_OF_MONTH, 6)
		add_start_date.text = sdf_date_show.format(startCal.time)
		add_end_date.text = sdf_date_show.format(endCal.time)

		add_start_date.setOnClickListener {
			DatePickerDialog(this@AddAlarm, { view, year, month, dayOfMonth ->
				startCal[Calendar.YEAR] = year
				startCal[Calendar.MONTH] = month
				startCal[Calendar.DAY_OF_MONTH] = dayOfMonth
				add_start_date.text = sdf_date_show.format(startCal.time)
			}, startCal[Calendar.YEAR], startCal[Calendar.MONTH], startCal[Calendar.DAY_OF_MONTH]).show()
		}

		add_end_date.setOnClickListener {
			DatePickerDialog(this@AddAlarm, { view, year, month, dayOfMonth ->
				endCal[Calendar.YEAR] = year
				endCal[Calendar.MONTH] = month
				endCal[Calendar.DAY_OF_MONTH] = dayOfMonth
				add_end_date.text = sdf_date_show.format(endCal.time)
			}, endCal[Calendar.YEAR], endCal[Calendar.MONTH], endCal[Calendar.DAY_OF_MONTH]).show()
		}

		// Setting repeats
		add_repeat_group.setOnCheckedChangeListener { radioGroup: RadioGroup, res_id: Int ->
			when (res_id) {
				R.id.add_repeat_everyday -> {
					add_date_sun.isChecked = true
					add_date_mon.isChecked = true
					add_date_tue.isChecked = true
					add_date_wed.isChecked = true
					add_date_thu.isChecked = true
					add_date_fri.isChecked = true
					add_date_sat.isChecked = true

					add_date_sun.isEnabled = false
					add_date_mon.isEnabled = false
					add_date_tue.isEnabled = false
					add_date_wed.isEnabled = false
					add_date_thu.isEnabled = false
					add_date_fri.isEnabled = false
					add_date_sat.isEnabled = false
				}
				R.id.add_repeat_select -> {
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

		// Setting labels
		val sql = """
			|SELECT _ID, TITLE, COLOR FROM LABELS
		""".trimMargin()

		val jAddLabel = JsonParser.parseString("""{"_ID":"", "TITLE":"+", "COLOR":"00000000"}""").asJsonObject
		val myHandler = DBHandler.open(this@AddAlarm)
		val lLabels = myHandler.execResult(sql)
		lLabels.add(jAddLabel)
		Log.i("@@@", lLabels.toString())
		val labelSize = lLabels.size()
		var count = 0
		var labelLine: AlarmLabelLine
		val labelBoxes = MutableList(4) { LinearLayout(this@AddAlarm) }
		var labelItem: AlarmLabelItem
		var labelText: TextView
		var jLabel: JsonObject
		var drawable: GradientDrawable

		while (count < labelSize) {
			labelLine = AlarmLabelLine(this@AddAlarm)
			add_label_container.addView(labelLine)
			for (i in 0..3) {
				if (count == labelSize) break
				jLabel = lLabels[i].asJsonObject
				labelBoxes[i] = findViewById<LinearLayout>(resources.getIdentifier("label_box_0${i}", "id", packageName))
				labelBoxes[i].id = resources.getIdentifier("label_box_${i}", "id", packageName)
				labelItem = AlarmLabelItem(this@AddAlarm)
				labelBoxes[i].addView(labelItem)
				labelText = findViewById<TextView>(R.id.add_label_item)
				labelText.id = resources.getIdentifier("label_item_${i}", "id", packageName)
				labelText.text = jLabel["TITLE"].asString
				drawable = labelText.background as GradientDrawable
				drawable.setColor(Color.parseColor("#${jLabel["COLOR"].asString}"))
				count++
			}
		}

		// TODO("Function for 'Add' button")

		// Setting submit the input
		submit_button.setOnClickListener {
			val count = add_times_switch.tag as Int

			val lTimes = mutableListOf<String>()
			for (i in 0..count - 1) {
				val holder =
					add_time_container.getChildViewHolder(add_time_container[i]) as AddTimeRecyclerAdapter.ViewHolder
				lTimes.add("\"${sdf_time_save.format(sdf_time_show.parse(holder.time.text.toString()))}\"")
			}
			lTimes.sort()
			lTimes.toString()

			val lRepeats = mutableListOf<Int>()
			if (add_date_sun.isChecked) lRepeats.add(1)
			if (add_date_mon.isChecked) lRepeats.add(2)
			if (add_date_tue.isChecked) lRepeats.add(3)
			if (add_date_wed.isChecked) lRepeats.add(4)
			if (add_date_thu.isChecked) lRepeats.add(5)
			if (add_date_fri.isChecked) lRepeats.add(6)
			if (add_date_sat.isChecked) lRepeats.add(7)

			if (lTimes.size > 0 && lRepeats.size == 0) {
				Toast.makeText(this@AddAlarm, "요일을 선택해 주세요", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			// TODO("Use label as title if title is empty")
			// TODO("Cannot add alarm if not label")

			val sql = """
				|INSERT INTO ALARMS (TITLE, START_DT, END_DT, TIMES, REPEATS, ALARM)
				|VALUES (
				|'${add_title.text}',
				|'${sdf_date_save.format(startCal.time)}',
				|'${sdf_date_save.format(endCal.time)}',
				|'[${lTimes.joinToString(",")}]',
				|'[${lRepeats.joinToString(",")}]',
				|${if (add_times_switch.isChecked) 1 else 0}
				|)
			""".trimMargin()
			Log.i("${packageName} - AddAlarm", sql)
			val mHandler = DBHandler.open(this@AddAlarm)
			if (mHandler.execNonResult(sql) == 0) {
				mHandler.close()
				finish()
			}
			mHandler.close()
		}
	}
}

class AddTimeRecyclerAdapter(context: Context?, lTimes: JsonArray) :
	RecyclerView.Adapter<AddTimeRecyclerAdapter.ViewHolder>() {
	val sdf_time_show = SimpleDateFormat("a h:mm", Locale.KOREA)
	val context: Context?
	val lTimes: JsonArray

	init {
		this.context = context
		this.lTimes = lTimes
	}

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val time = itemView.findViewById<TextView>(R.id.time_item)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(context).inflate(R.layout.alarm_time_item, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return lTimes.size()
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.time.text = lTimes[position].asString
		holder.time.setOnClickListener {
			val cal = Calendar.getInstance()
			cal.time = sdf_time_show.parse(holder.time.text.toString())
			TimePickerDialog(context, { timePicker: TimePicker, hourOfDay: Int, minute: Int ->
				cal[Calendar.HOUR_OF_DAY] = hourOfDay
				cal[Calendar.MINUTE] = minute
				holder.time.text = sdf_time_show.format(cal.time)
			}, cal[Calendar.HOUR_OF_DAY], cal[Calendar.MINUTE], false).show()
		}
	}
}