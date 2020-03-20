package com.khnsoft.medihand

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableContainer
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.add_alarm_activity.*
import kotlinx.android.synthetic.main.add_alarm_activity.back_btn
import java.io.File
import java.util.*

class AddAlarmActivity : AppCompatActivity() {

	val DEFAULT_TIMES = JsonParser.parseString(
		"""
		[
			[],
			["오전 9:00"],
			["오전 9:00", "오후 7:00"],
			["오전 9:00", "오후 1:00", "오후 7:00"],
			["오전 9:00", "오전 11:00", "오후 1:00", "오후 3:00", "오후 5:00", "오후 7:00"],
			["오전 9:00", "오전 11:00", "오후 1:00", "오후 3:00", "오후 5:00", "오후 7:00", "오후 9:00", "오후 9:00"],
			["오전 9:00", "오전 11:00", "오후 1:00", "오후 3:00", "오후 5:00", "오후 7:00", "오후 9:00", "오후 9:00", "오후 9:00", "오후 9:00", "오후 9:00", "오후 9:00"]
		]
		""".trimIndent()
	).asJsonArray

	lateinit var radioGroup: MyRadioGroup
	var labelSelected = -1

	val images = mutableListOf<Bitmap>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.add_alarm_activity)

		// Setting back button
		back_btn.setOnClickListener {
			onBackPressed()
		}

		// Setting image button
		image_section.visibility = View.GONE
		/*
		if (UserData.careUser == null) {
			image_from_camera.setOnClickListener {
				val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
				startActivityForResult(intent, PhotoAttr.CAMERA.rc)
			}

			image_from_gallery.setOnClickListener {
				val intent = Intent(Intent.ACTION_PICK)
				intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
				startActivityForResult(intent, PhotoAttr.GALLERY.rc)
			}
		} else {
			image_section.visibility = View.GONE
		}
		 */

		// Setting alarm time
		val alarm_counts_array = resources.getStringArray(R.array.alarm_times)
		val arrayAdapter =
			ArrayAdapter<String>(applicationContext, R.layout.alarm_count_spinner_item, alarm_counts_array)
		arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
		alarm_times.adapter = arrayAdapter

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

				val adapter = AddTimeRecyclerAdapter(DEFAULT_TIMES[position].asJsonArray)
				val lm = LinearLayoutManager(this@AddAlarmActivity)
				add_time_container.layoutManager = lm
				add_time_container.adapter = adapter
			}
		}
		alarm_times.setSelection(3)

		// Setting start and end date
		val startCal = Calendar.getInstance()
		val endCal = Calendar.getInstance()
		endCal.time = startCal.time
		endCal.add(Calendar.DAY_OF_MONTH, 6)
		add_start_date.text = SDF.dateInKorean.format(startCal.time)
		add_end_date.text = SDF.dateInKorean.format(endCal.time)

		add_start_date.setOnClickListener {
			DatePickerDialog(this@AddAlarmActivity, { view, year, month, dayOfMonth ->
				startCal[Calendar.YEAR] = year
				startCal[Calendar.MONTH] = month
				startCal[Calendar.DAY_OF_MONTH] = dayOfMonth
				add_start_date.text = SDF.dateInKorean.format(startCal.time)

				if (startCal.timeInMillis > endCal.timeInMillis) {
					endCal[Calendar.YEAR] = year
					endCal[Calendar.MONTH] = month
					endCal[Calendar.DAY_OF_MONTH] = dayOfMonth
					add_end_date.text = SDF.dateInKorean.format(endCal.time)
				}
			}, startCal[Calendar.YEAR], startCal[Calendar.MONTH], startCal[Calendar.DAY_OF_MONTH]).show()
		}

		add_end_date.setOnClickListener {
			DatePickerDialog(this@AddAlarmActivity, { view, year, month, dayOfMonth ->
				endCal[Calendar.YEAR] = year
				endCal[Calendar.MONTH] = month
				endCal[Calendar.DAY_OF_MONTH] = dayOfMonth
				add_end_date.text = SDF.dateInKorean.format(endCal.time)

				if (startCal.timeInMillis > endCal.timeInMillis) {
					startCal[Calendar.YEAR] = year
					startCal[Calendar.MONTH] = month
					startCal[Calendar.DAY_OF_MONTH] = dayOfMonth
					add_start_date.text = SDF.dateInKorean.format(startCal.time)
				}
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

		// Setting submit the input
		submit_btn.setOnClickListener {
			val count = add_times_switch.tag as Int

			if (radioGroup.getCheckedIndex() == -1) {
				Toast.makeText(this@AddAlarmActivity, "증상을 선택해 주세요.", Toast.LENGTH_LONG).show()
				return@setOnClickListener
			}

			val lTimes = mutableListOf<String>()
			val adapter = add_time_container.adapter as AddTimeRecyclerAdapter
			for (i in 0..count-1) {
				lTimes.add(SDF.time.format(SDF.timeInKorean.parse(adapter.curTimes[i])))
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
				Toast.makeText(this@AddAlarmActivity, "요일을 선택해 주세요", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			if (add_title.text.isBlank()) add_title.setText(radioGroup.radios[radioGroup.getCheckedIndex()].text)

			val curCal = Calendar.getInstance()

			val json = JsonObject()
			json.addProperty("alarm_title", add_title.text.toString())
			json.addProperty("alarm_user", UserData.careUser ?: UserData.id)
			json.addProperty("alarm_label", radioGroup.radios[radioGroup.getCheckedIndex()].tag as Int)
			json.addProperty("alarm_start_date", SDF.dateBar.format(startCal.time))
			json.addProperty("alarm_end_date", SDF.dateBar.format(endCal.time))
			json.addProperty("alarm_times", lTimes.joinToString("/"))
			json.addProperty("alarm_repeats", lRepeats.joinToString("/"))
			json.addProperty(
				"alarm_enabled",
				if (add_times_switch.isChecked) AlarmStatus.ENABLE.name else AlarmStatus.CANCEL.name
			)

			val result =
				if (UserData.careUser == null) ServerHandler.send(this@AddAlarmActivity, EndOfAPI.ADD_ALARM, json)
				else ServerHandler.send(this@AddAlarmActivity, EndOfAPI.SYNC_ADD_ALARM, json, UserData.careUser)

			if (!HttpHelper.isOK(result)) {
				Toast.makeText(this@AddAlarmActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			val jData = result["data"].asJsonObject
			val alarmId = jData["alarmId"].asInt

			if (UserData.careUser == null) {
				/*
				val json2 = JsonObject()
				val lImages = JsonArray()

				for (image in images) {
					lImages.add(ImageHelper.bitmapToBase64(image))
				}

				json2.add("image", lImages)
				val result2 = ServerHandler.send(this@AddAlarmActivity, EndOfAPI.ADD_IMAGE, json2, alarmId)
				if (!HttpHelper.isOK(result2)) return@setOnClickListener
				 */
				val sp = SPHandler.getSp(this@AddAlarmActivity)
				val lList = JsonParser.parseString(sp.getString(AlarmReceiver.SP_ALL_ID, "{}")).asJsonObject
				lList.add(alarmId.toString(), null)
				val editor = sp.edit()
				editor.putString(AlarmReceiver.SP_ALL_ID, lList.toString())
				editor.apply()

				json.addProperty("alarm_id", alarmId)
				AlarmHandler.createAlarm(this@AddAlarmActivity, json)
			} else {
				val data = MyAlertPopup.Data(AlertType.CONFIRM)
				data.alertTitle = "추가 성공"
				data.alertContent = "변경 사항이 적용되려면 크루의 어플을 한 번 재실행 해야합니다."
				val dataId = DataPasser.insert(data)

				val intent = Intent(this@AddAlarmActivity, MyAlertPopup::class.java)
				intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
				startActivity(intent)
			}

			finish()
		}
	}

	fun refreshImages() {
		val adapter = PhotoRecyclerAdapter()
		val lm = LinearLayoutManager(this@AddAlarmActivity, LinearLayoutManager.HORIZONTAL, false)
		image_container.layoutManager = lm
		image_container.adapter = adapter
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == PhotoAttr.CAMERA.rc) {
			if (resultCode == Activity.RESULT_OK) {
				val img = (data?.extras?.get("data") ?: return) as Bitmap
				images.add(img)
				refreshImages()
			}
		} else if (requestCode == PhotoAttr.GALLERY.rc) {
			if (data == null) return
			val photoUri = data.data
			var cursor: Cursor? = null
			val tempFile: File?

			try {
				val proj = arrayOf(MediaStore.Images.Media.DATA)

				cursor = contentResolver.query(photoUri ?: return, proj, null, null, null)

				val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA) ?: return

				cursor.moveToFirst()
				tempFile = File(cursor.getString(column_index))
			} finally {
				cursor?.close()
			}

			val options = BitmapFactory.Options()
			val img = BitmapFactory.decodeFile(tempFile?.absolutePath ?: return, options)
			images.add(img)

			refreshImages()
		}
	}

	override fun onResume() {
		super.onResume()
		setupLabels()
	}

	fun setupLabels() {
		radioGroup = MyRadioGroup()
		radioGroup.setOnChangeListener(LabelSelectedListener)

		val result = if (UserData.careUser == null) ServerHandler.send(this@AddAlarmActivity, EndOfAPI.GET_LABELS)
		else ServerHandler.send(this@AddAlarmActivity, EndOfAPI.SYNC_GET_LABELS, id=UserData.careUser)

		if (!HttpHelper.isOK(result)) {
			Toast.makeText(this@AddAlarmActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
			finish()
		}
		val lLabels = result["data"].asJsonArray

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

		add_label_container.removeAllViews()
		label@ while (count < labelSize) {
			labelLine = AlarmLabelLine(this@AddAlarmActivity)
			add_label_container.addView(labelLine)
			for (i in 0..3) {
				labelBox = findViewById<LinearLayout>(resources.getIdentifier("label_box_0${i}", "id", packageName))
				labelBox.id = 0
				labelItem = AlarmLabelItem(this@AddAlarmActivity)
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
			labelLine = AlarmLabelLine(this@AddAlarmActivity)
			add_label_container.addView(labelLine)
		}

		labelBox = findViewById<LinearLayout>(resources.getIdentifier("label_box_0${count % 4}", "id", packageName))

		val addLabelbox = AlarmLabelPlus(this@AddAlarmActivity)
		labelBox?.addView(addLabelbox)
		val addLabelBtn = findViewById<TextView>(R.id.add_label_item)

		addLabelBtn.setOnClickListener {
			val intent = Intent(this@AddAlarmActivity, AddLabelPopup::class.java)
			startActivity(intent)
		}

		if (labelSelected != -1) radioGroup.getButtonByTag(labelSelected)?.callOnClick()
	}

	val LabelSelectedListener = object : MyRadioGroup.OnChangeListener {
		override fun onChange(after: RadioButton) {
			labelSelected = radioGroup.getCheckedTag()
		}
	}

	inner class AddTimeRecyclerAdapter(val lTimes: JsonArray) :
		RecyclerView.Adapter<AddTimeRecyclerAdapter.ViewHolder>() {
		val curTimes = mutableListOf<String>()

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val time = itemView.findViewById<TextView>(R.id.time_item)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(this@AddAlarmActivity).inflate(R.layout.alarm_time_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lTimes.size()
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			val sTime = lTimes[position].asString
			if (curTimes.size < lTimes.size()) curTimes.add(sTime)
			holder.time.text = sTime
			holder.time.setOnClickListener {
				val cal = Calendar.getInstance()
				cal.time = SDF.timeInKorean.parse(holder.time.text.toString())
				TimePickerDialog(this@AddAlarmActivity, { timePicker: TimePicker, hourOfDay: Int, minute: Int ->
					cal[Calendar.HOUR_OF_DAY] = hourOfDay
					cal[Calendar.MINUTE] = minute
					val sTime = SDF.timeInKorean.format(cal.time)
					curTimes.set(position, sTime)
					holder.time.text = sTime
				}, cal[Calendar.HOUR_OF_DAY], cal[Calendar.MINUTE], false).show()
			}
		}
	}

	inner class PhotoRecyclerAdapter() :
		RecyclerView.Adapter<PhotoRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val image = itemView.findViewById<ImageView>(R.id.image_item)
			val remove = itemView.findViewById<ImageView>(R.id.image_remove)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(this@AddAlarmActivity).inflate(R.layout.alarm_page_photo_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return images.size
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			val bitmap = images[position]
			holder.image.setImageBitmap(bitmap)
			holder.remove.setOnClickListener {
				images.removeAt(position)
				refreshImages()
			}
		}
	}
}