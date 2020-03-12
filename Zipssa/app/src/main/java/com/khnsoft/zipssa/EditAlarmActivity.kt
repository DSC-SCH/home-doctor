package com.khnsoft.zipssa

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableContainer
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
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
import kotlinx.android.synthetic.main.edit_alarm_activity.*
import java.text.SimpleDateFormat
import java.util.*

class EditAlarmActivity : AppCompatActivity() {
	val sdf_time_show = SimpleDateFormat("a hh:mm", Locale.KOREA)
	val sdf_date_show = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
	val sdf_time_save = SimpleDateFormat("HH:mm", Locale.KOREA)
	val sdf_date_save = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

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

	lateinit var radioGroup: MyRadioGroup

	var timesInitialized = false
	var labelSelected = -1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.edit_alarm_activity)

		val _intent = intent
		val _jItem = JsonParser.parseString(_intent.getStringExtra("jItem")).asJsonObject


		// Setting back button
		back_btn.setOnClickListener {
			onBackPressed()
		}

		// Setting title
		edit_title.setText(_jItem["alarm_title"].asString)

		// Setting alarm time
		val alarm_counts_array = resources.getStringArray(R.array.alarm_times)
		val arrayAdapter = ArrayAdapter<String>(applicationContext, R.layout.alarm_count_spinner_item, alarm_counts_array)
		arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
		alarm_times.adapter = arrayAdapter

		edit_times_switch.isChecked = AlarmParser.parseStatus(_jItem["alarm_enabled"].asString) == AlarmStatus.ENABLED

		val _jTimesRaw = AlarmParser.parseTimes(_jItem["alarm_times"].asString)
		val _jTimes = JsonArray()
		for (item in _jTimesRaw) {
			_jTimes.add(sdf_time_show.format(sdf_time_save.parse(item.asString)))
		}
		alarm_times.setSelection(when(_jTimes.size()) {
			1 -> 1
			2 -> 2
			3 -> 3
			6 -> 4
			8 -> 5
			12 -> 6
			else -> 0
		})

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
				edit_times_switch.tag = count

				val adapter: EditTimeRecyclerAdapter
				if (!timesInitialized) {
					adapter = EditTimeRecyclerAdapter(_jTimes)
				} else {
					adapter = EditTimeRecyclerAdapter(DEFAULT_TIMES[position].asJsonArray)
				}
				val lm = LinearLayoutManager(this@EditAlarmActivity)
				edit_time_container.layoutManager = lm
				edit_time_container.adapter = adapter
			}
		}

		// Setting start and end date
		val startCal = Calendar.getInstance()
		startCal.time = sdf_date_save.parse(_jItem["alarm_start_date"].asString)
		val endCal = Calendar.getInstance()
		endCal.time = sdf_date_save.parse(_jItem["alarm_end_date"].asString)
		edit_start_date.text = sdf_date_show.format(startCal.time)
		edit_end_date.text = sdf_date_show.format(endCal.time)

		edit_start_date.setOnClickListener {
			DatePickerDialog(this@EditAlarmActivity, { view, year, month, dayOfMonth ->
				startCal[Calendar.YEAR] = year
				startCal[Calendar.MONTH] = month
				startCal[Calendar.DAY_OF_MONTH] = dayOfMonth
				edit_start_date.text = sdf_date_show.format(startCal.time)
			}, startCal[Calendar.YEAR], startCal[Calendar.MONTH], startCal[Calendar.DAY_OF_MONTH]).show()
		}

		edit_end_date.setOnClickListener {
			DatePickerDialog(this@EditAlarmActivity, { view, year, month, dayOfMonth ->
				endCal[Calendar.YEAR] = year
				endCal[Calendar.MONTH] = month
				endCal[Calendar.DAY_OF_MONTH] = dayOfMonth
				edit_end_date.text = sdf_date_show.format(endCal.time)
			}, endCal[Calendar.YEAR], endCal[Calendar.MONTH], endCal[Calendar.DAY_OF_MONTH]).show()
		}

		// Setting repeats
		edit_repeat_group.setOnCheckedChangeListener { radioGroup: RadioGroup, res_id: Int ->
			when (res_id) {
				R.id.edit_repeat_everyday -> {
					edit_date_sun.isChecked = true
					edit_date_mon.isChecked = true
					edit_date_tue.isChecked = true
					edit_date_wed.isChecked = true
					edit_date_thu.isChecked = true
					edit_date_fri.isChecked = true
					edit_date_sat.isChecked = true

					edit_date_sun.isEnabled = false
					edit_date_mon.isEnabled = false
					edit_date_tue.isEnabled = false
					edit_date_wed.isEnabled = false
					edit_date_thu.isEnabled = false
					edit_date_fri.isEnabled = false
					edit_date_sat.isEnabled = false
				}
				R.id.edit_repeat_select -> {
					edit_date_sun.isEnabled = true
					edit_date_mon.isEnabled = true
					edit_date_tue.isEnabled = true
					edit_date_wed.isEnabled = true
					edit_date_thu.isEnabled = true
					edit_date_fri.isEnabled = true
					edit_date_sat.isEnabled = true
				}
			}
		}
		val _jRepeats = AlarmParser.parseRepeats(_jItem["alarm_repeats"].asString)

		if (_jRepeats.size() == 7) {
			edit_repeat_group.check(R.id.edit_repeat_everyday)
		} else {
			edit_repeat_group.check(R.id.edit_repeat_select)
			edit_date_sun.isChecked = false
			edit_date_mon.isChecked = false
			edit_date_tue.isChecked = false
			edit_date_wed.isChecked = false
			edit_date_thu.isChecked = false
			edit_date_fri.isChecked = false
			edit_date_sat.isChecked = false
			for (i in _jRepeats) {
				when (i.asInt) {
					1 -> edit_date_sun.isChecked = true
					2 -> edit_date_mon.isChecked = true
					3 -> edit_date_tue.isChecked = true
					4 -> edit_date_wed.isChecked = true
					5 -> edit_date_thu.isChecked = true
					6 -> edit_date_fri.isChecked = true
					7 -> edit_date_sat.isChecked = true
				}
			}
		}

		// Setting labels
		labelSelected = _jItem["alarm_label"].asInt

		// Setting delete button
		edit_delete_btn.setOnClickListener {
			val data = MyAlertPopup.Data(AlertType.ALERT).apply {
				alertTitle = edit_title.text.toString()
				alertContent = "해당 알림 정보를 삭제하시겠습니까?"
				alertConfirmText = "삭제"
				confirmListener = View.OnClickListener {
					val result = ServerHandler.send(this@EditAlarmActivity, EndOfAPI.DELETE_ALARM, id=_jItem["alarm_id"].asInt)
					if (!HttpAttr.isOK(result)) {
						return@OnClickListener
					}

					val data = MyAlertPopup.Data(AlertType.CONFIRM)
					data.alertTitle = alertTitle
					data.alertContent = "해당 알림이 삭제되었습니다."
					val dataId = DataPasser.insert(data)

					val intent = Intent(this@EditAlarmActivity, MyAlertPopup::class.java)
					intent.putExtra(MyAlertPopup.EXTRA_DATA, dataId)
					startActivity(intent)
				}
			}

			val dataId = DataPasser.insert(data)
			val intent = Intent(this@EditAlarmActivity, MyAlertPopup::class.java)
			intent.putExtra(MyAlertPopup.EXTRA_DATA, dataId)
			startActivityForResult(intent, MyAlertPopup.RC)
		}

		// Setting edit button
		edit_confirm_btn.setOnClickListener {
			val count = edit_times_switch.tag as Int

			if (radioGroup.getCheckedIndex() == -1) {
				Toast.makeText(this@EditAlarmActivity, "증상을 선택해 주세요.", Toast.LENGTH_LONG).show()
				return@setOnClickListener
			}

			val lTimes = mutableListOf<String>()
			for (i in 0..count - 1) {
				val holder =
					edit_time_container.getChildViewHolder(edit_time_container[i]) as EditTimeRecyclerAdapter.ViewHolder
				lTimes.add(sdf_time_save.format(sdf_time_show.parse(holder.time.text.toString())))
			}
			lTimes.sort()
			lTimes.toString()

			val lRepeats = mutableListOf<Int>()
			if (edit_date_sun.isChecked) lRepeats.add(1)
			if (edit_date_mon.isChecked) lRepeats.add(2)
			if (edit_date_tue.isChecked) lRepeats.add(3)
			if (edit_date_wed.isChecked) lRepeats.add(4)
			if (edit_date_thu.isChecked) lRepeats.add(5)
			if (edit_date_fri.isChecked) lRepeats.add(6)
			if (edit_date_sat.isChecked) lRepeats.add(7)

			if (lTimes.size > 0 && lRepeats.size == 0) {
				Toast.makeText(this@EditAlarmActivity, "요일을 선택해 주세요", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			if (edit_title.text.isBlank()) edit_title.setText(radioGroup.radios[radioGroup.getCheckedIndex()].text)

			val data = MyAlertPopup.Data(AlertType.ALERT).apply {
				alertTitle = edit_title.text.toString()
				alertContent = "해당 알림 정보를 수정하시겠습니까?"
				alertConfirmText = "수정"
				confirmListener = View.OnClickListener {
					val curCal = Calendar.getInstance()
					val json = JsonObject()
					json.addProperty("alarm_title", edit_title.text.toString())
					json.addProperty("alarm_label", radioGroup.radios[radioGroup.getCheckedIndex()].tag as Int)
					json.addProperty("alarm_start_date", sdf_date_save.format(startCal.time))
					json.addProperty("alarm_end_date", sdf_date_save.format(endCal.time))
					json.addProperty("alarm_times", lTimes.joinToString("/"))
					json.addProperty("alarm_repeats", lRepeats.joinToString("/"))
					json.addProperty("alarm_enabled", if (edit_times_switch.isChecked) AlarmStatus.ENABLED.status else AlarmStatus.DISABLED.status)
					json.addProperty("last_modified_date", sdf_date_save.format(curCal.time))

					val result = ServerHandler.send(this@EditAlarmActivity, EndOfAPI.EDIT_ALARM, json, _jItem["alarm_id"].asInt)
					if (!HttpAttr.isOK(result)) return@OnClickListener

					val data = MyAlertPopup.Data(AlertType.CONFIRM)
					data.alertTitle = alertTitle
					data.alertContent = "해당 알림이 수정되었습니다."
					val dataId = DataPasser.insert(data)

					val intent = Intent(this@EditAlarmActivity, MyAlertPopup::class.java)
					intent.putExtra(MyAlertPopup.EXTRA_DATA, dataId)
					startActivity(intent)
				}
			}

			val dataId = DataPasser.insert(data)
			val intent = Intent(this@EditAlarmActivity, MyAlertPopup::class.java)
			intent.putExtra(MyAlertPopup.EXTRA_DATA, dataId)
			startActivityForResult(intent, MyAlertPopup.RC)
		}
	}

	override fun onResume() {
		super.onResume()
		setupLabels()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == MyAlertPopup.RC) {
			if (data != null && data.getIntExtra(MyAlertPopup.EXTRA_RESULT, StatusCode.FAILED.status) == StatusCode.SUCCESS.status)
				finish()
		}
	}

	fun setupLabels() {
		val sql = """
			|SELECT _ID, LABEL_TITLE, LABEL_COLOR FROM LABEL_TB
		""".trimMargin()

		radioGroup = MyRadioGroup()
		radioGroup.setOnChangeListener(LabelSelectedListener)

		val lLabels = ServerHandler.send(this@EditAlarmActivity, EndOfAPI.GET_LABELS)["data"].asJsonArray

		val labelSize = lLabels.size()
		var count = 0
		var labelLine: AlarmLabelLine
		var labelBox: LinearLayout? = null
		var labelItem: AlarmLabelItem
		var labelRadioBtn: RadioButton
		var jLabel: JsonObject
		var drawable: StateListDrawable
		var drawableState: DrawableContainer.DrawableContainerState
		var children: Array<Drawable>
		var selected: GradientDrawable
		var unselected: GradientDrawable

		edit_label_container.removeAllViews()
		label@while (count < labelSize) {
			labelLine = AlarmLabelLine(this@EditAlarmActivity)
			edit_label_container.addView(labelLine)
			for (i in 0..3) {
				labelBox = findViewById<LinearLayout>(resources.getIdentifier("label_box_0${i}", "id", packageName))
				labelBox.id = 0
				labelItem = AlarmLabelItem(this@EditAlarmActivity)
				labelBox.addView(labelItem)
				labelRadioBtn = findViewById<RadioButton>(R.id.add_label_item)
				labelRadioBtn.id = 0
				radioGroup.add(labelRadioBtn)

				jLabel = ServerHandler.convertKeys(lLabels[count].asJsonObject, ServerHandler.labelToLocal)
				labelRadioBtn.tag = jLabel["label_id"].asInt
				labelRadioBtn.text = jLabel["label_title"].asString

				// Setting colors
				drawable = labelRadioBtn.background as StateListDrawable
				drawableState = drawable.constantState as DrawableContainer.DrawableContainerState
				children = drawableState.children
				selected = children[0] as GradientDrawable
				selected.setColor(Color.parseColor("${jLabel["label_color"].asString}"))
				unselected = children[1] as GradientDrawable
				unselected.setColor(Color.parseColor("${jLabel["label_color"].asString}"))

				count++
				when (count) {
					1 -> labelRadioBtn.isChecked = true
					labelSize -> break@label
				}
			}
		}

		if (count % 4 == 0) {
			labelLine = AlarmLabelLine(this@EditAlarmActivity)
			edit_label_container.addView(labelLine)
		}

		labelBox = findViewById<LinearLayout>(resources.getIdentifier("label_box_0${count%4}", "id", packageName))

		val addLabelbox = AlarmLabelPlus(this@EditAlarmActivity)
		labelBox?.addView(addLabelbox)
		val addLabelBtn = findViewById<TextView>(R.id.add_label_item)

		addLabelBtn.setOnClickListener {
			val intent = Intent(this@EditAlarmActivity, AddLabelPopup::class.java)
			startActivity(intent)
		}

		if (labelSelected != -1) radioGroup.getButtonByTag(labelSelected)?.callOnClick()
	}

	val LabelSelectedListener = object : MyRadioGroup.OnChangeListener {
		override fun onChange(after: RadioButton) {
			labelSelected = radioGroup.getCheckedTag()
		}
	}

	inner class EditTimeRecyclerAdapter(val lTimes: JsonArray) :
		RecyclerView.Adapter<EditTimeRecyclerAdapter.ViewHolder>() {
		val sdf_time_show = SimpleDateFormat("a h:mm", Locale.KOREA)

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val time = itemView.findViewById<TextView>(R.id.time_item)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(this@EditAlarmActivity).inflate(R.layout.alarm_time_item, parent, false)
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
				TimePickerDialog(this@EditAlarmActivity, { timePicker: TimePicker, hourOfDay: Int, minute: Int ->
					cal[Calendar.HOUR_OF_DAY] = hourOfDay
					cal[Calendar.MINUTE] = minute
					holder.time.text = sdf_time_show.format(cal.time)
				}, cal[Calendar.HOUR_OF_DAY], cal[Calendar.MINUTE], false).show()
			}
		}
	}
}