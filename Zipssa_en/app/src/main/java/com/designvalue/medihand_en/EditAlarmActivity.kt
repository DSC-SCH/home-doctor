package com.designvalue.medihand_en

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
import kotlinx.android.synthetic.main.edit_alarm_activity.*
import java.io.File
import java.util.*

class EditAlarmActivity : AppCompatActivity() {

	val DEFAULT_TIMES = JsonParser.parseString(
		"""
		[
			[],
			["AM 9:00"],
			["AM 9:00", "PM 7:00"],
			["AM 9:00", "PM 1:00", "PM 7:00"],
			["AM 9:00", "AM 11:00", "PM 1:00", "PM 3:00", "PM 5:00", "PM 7:00"],
			["AM 9:00", "AM 11:00", "PM 1:00", "PM 3:00", "PM 5:00", "PM 7:00", "PM 9:00", "PM 9:00"],
			["AM 9:00", "AM 11:00", "PM 1:00", "PM 3:00", "PM 5:00", "PM 7:00", "PM 9:00", "PM 9:00", "PM 9:00", "PM 9:00", "PM 9:00", "PM 9:00"]
		]
		""".trimIndent()
	).asJsonArray

	lateinit var radioGroup: MyRadioGroup

	var timesInitialized = false
	var labelSelected = -1

	val images = mutableListOf<Bitmap>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.edit_alarm_activity)

		val _id = intent.getIntExtra(ExtraAttr.ALARM_ID, -1)
		val result = if (UserData.careUser == null) ServerHandler.send(this@EditAlarmActivity, EndOfAPI.GET_ALARM, id=_id)
		else ServerHandler.send(this@EditAlarmActivity, EndOfAPI.SYNC_GET_ALARM, id=UserData.careUser, id2=_id)

		if (!HttpHelper.isOK(result)) {
			Toast.makeText(this@EditAlarmActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
			finish()
		}
		val _jTemp = result["data"].asJsonObject
		val _jItem = ServerHandler.convertKeys(_jTemp, ServerHandler.alarmToLocal)

		// Setting back button
		back_btn.setOnClickListener {
			onBackPressed()
		}

		image_section.visibility = View.GONE
		/*
		// Setting image
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

			val result = ServerHandler.send(this@EditAlarmActivity, EndOfAPI.GET_IMAGES, id = _id)

			if (!HttpHelper.isOK(result)) finish()
			val lImages = result["data"].asJsonArray
			for (image in lImages) {
				val jItem = ServerHandler.convertKeys(image.asJsonObject, ServerHandler.imageToLocal)
				images.add(ImageHelper.base64ToBitmap(jItem["image"].asString))
			}
			refreshImages()
		} else {
			image_section.visibility = View.GONE
		}
		 */

		// Setting title
		edit_title.setText(_jItem["alarm_title"].asString)

		// Setting alarm time
		val alarm_counts_array = resources.getStringArray(R.array.alarm_times)
		val arrayAdapter = ArrayAdapter<String>(applicationContext, R.layout.alarm_count_spinner_item, alarm_counts_array)
		arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
		alarm_times.adapter = arrayAdapter

		edit_times_switch.isChecked = AlarmParser.parseStatus(_jItem["alarm_enabled"].asString) == AlarmStatus.ENABLE
		if (UserData.careUser != null) edit_times_switch.isEnabled = false

		val _jTimesRaw = AlarmParser.parseTimes(_jItem["alarm_times"].asString)
		val _jTimes = JsonArray()
		for (item in _jTimesRaw) {
			_jTimes.add(SDF.timeInKorean.format(SDF.time.parse(item.asString)))
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
					timesInitialized = !timesInitialized
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
		startCal.time = SDF.dateBar.parse(_jItem["alarm_start_date"].asString)
		val endCal = Calendar.getInstance()
		endCal.time = SDF.dateBar.parse(_jItem["alarm_end_date"].asString)
		edit_start_date.text = SDF.dateInKorean.format(startCal.time)
		edit_end_date.text = SDF.dateInKorean.format(endCal.time)

		edit_start_date.setOnClickListener {
			DatePickerDialog(this@EditAlarmActivity, { view, year, month, dayOfMonth ->
				startCal[Calendar.YEAR] = year
				startCal[Calendar.MONTH] = month
				startCal[Calendar.DAY_OF_MONTH] = dayOfMonth
				edit_start_date.text = SDF.dateInKorean.format(startCal.time)
			}, startCal[Calendar.YEAR], startCal[Calendar.MONTH], startCal[Calendar.DAY_OF_MONTH]).show()
		}

		edit_end_date.setOnClickListener {
			DatePickerDialog(this@EditAlarmActivity, { view, year, month, dayOfMonth ->
				endCal[Calendar.YEAR] = year
				endCal[Calendar.MONTH] = month
				endCal[Calendar.DAY_OF_MONTH] = dayOfMonth
				edit_end_date.text = SDF.dateInKorean.format(endCal.time)
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
				alertContent = "Are you sure to delete this alarm?"
				alertConfirmText = "Delete"
				confirmListener = View.OnClickListener {
					val result = if (UserData.careUser == null) ServerHandler.send(this@EditAlarmActivity, EndOfAPI.DELETE_ALARM, id=_id)
					else ServerHandler.send(this@EditAlarmActivity, EndOfAPI.SYNC_DELETE_ALARM, id=UserData.careUser, id2=_id)
					if (!HttpHelper.isOK(result)) {
						Toast.makeText(this@EditAlarmActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
						return@OnClickListener
					}

					if (UserData.careUser == null) {
						AlarmHandler.clearAlarmById(this@EditAlarmActivity, _id)

						val data = MyAlertPopup.Data(AlertType.CONFIRM)
						data.alertTitle = alertTitle
						data.alertContent = "The alarm has been deleted."
						val dataId = DataPasser.insert(data)

						val intent = Intent(this@EditAlarmActivity, MyAlertPopup::class.java)
						intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
						startActivity(intent)
					} else {
						val data = MyAlertPopup.Data(AlertType.CONFIRM)
						data.alertTitle = alertTitle
						data.alertContent = "The alarm has been deleted. The crew needs to re-run the application once to apply the changes."
						val dataId = DataPasser.insert(data)

						val intent = Intent(this@EditAlarmActivity, MyAlertPopup::class.java)
						intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
						startActivity(intent)
					}
				}
			}

			val dataId = DataPasser.insert(data)
			val intent = Intent(this@EditAlarmActivity, MyAlertPopup::class.java)
			intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
			startActivityForResult(intent, MyAlertPopup.RC)
		}

		// Setting edit button
		edit_confirm_btn.setOnClickListener {
			val count = edit_times_switch.tag as Int

			if (radioGroup.getCheckedIndex() == -1) {
				Toast.makeText(this@EditAlarmActivity, "Please choose the symptom.", Toast.LENGTH_LONG).show()
				return@setOnClickListener
			}

			val lTimes = mutableListOf<String>()
			val adapter = edit_time_container.adapter as EditTimeRecyclerAdapter
			for (i in 0..count - 1) {
				lTimes.add(SDF.time.format(SDF.timeInKorean.parse(adapter.curTimes[i])))
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
				Toast.makeText(this@EditAlarmActivity, "Please choose days of the week.", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			if (edit_title.text.isBlank()) edit_title.setText(radioGroup.radios[radioGroup.getCheckedIndex()].text)

			val data = MyAlertPopup.Data(AlertType.ALERT).apply {
				alertTitle = edit_title.text.toString()
				alertContent = "Are you sure to modify this alarm?"
				alertConfirmText = "Modify"
				confirmListener = View.OnClickListener {
					val curCal = Calendar.getInstance()
					val json = JsonObject()
					json.addProperty("alarm_title", edit_title.text.toString())
					json.addProperty("alarm_label", radioGroup.radios[radioGroup.getCheckedIndex()].tag as Int)
					json.addProperty("alarm_start_date", SDF.dateBar.format(startCal.time))
					json.addProperty("alarm_end_date", SDF.dateBar.format(endCal.time))
					json.addProperty("alarm_times", lTimes.joinToString("/"))
					json.addProperty("alarm_repeats", lRepeats.joinToString("/"))
					json.addProperty("alarm_enabled", if (edit_times_switch.isChecked) AlarmStatus.ENABLE.name else AlarmStatus.CANCEL.name)

					val result = if (UserData.careUser == null) ServerHandler.send(this@EditAlarmActivity, EndOfAPI.EDIT_ALARM, json, _id)
					else ServerHandler.send(this@EditAlarmActivity, EndOfAPI.SYNC_EDIT_ALARM, json, UserData.careUser, _id)

					if (!HttpHelper.isOK(result)) {
						Toast.makeText(this@EditAlarmActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
						return@OnClickListener
					}

					if (UserData.careUser == null) {
						/*
						val json2 = JsonObject()
						val lImages = JsonArray()

						for (image in images) {
							lImages.add(ImageHelper.bitmapToBase64(image))
						}

						json2.add("image", lImages)
						val result2 = ServerHandler.send(this@EditAlarmActivity, EndOfAPI.EDIT_IMAGES, json2, _id)

						if (!HttpHelper.isOK(result2)) return@OnClickListener
						 */

						json.addProperty("alarm_id", _id)
						AlarmHandler.clearAlarmById(this@EditAlarmActivity, _id)
						AlarmHandler.createAlarm(this@EditAlarmActivity, json)
					}

					val data = MyAlertPopup.Data(AlertType.CONFIRM)
					data.alertTitle = alertTitle
					data.alertContent = "The alarm has been modified."
					val dataId = DataPasser.insert(data)

					val intent = Intent(this@EditAlarmActivity, MyAlertPopup::class.java)
					intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
					startActivity(intent)
				}
			}

			val dataId = DataPasser.insert(data)
			val intent = Intent(this@EditAlarmActivity, MyAlertPopup::class.java)
			intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
			startActivityForResult(intent, MyAlertPopup.RC)
		}
	}

	override fun onResume() {
		super.onResume()
		setupLabels()
	}

	fun refreshImages() {
		val adapter = PhotoRecyclerAdapter()
		val lm = LinearLayoutManager(this@EditAlarmActivity, LinearLayoutManager.HORIZONTAL, false)
		image_container.layoutManager = lm
		image_container.adapter = adapter
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == MyAlertPopup.RC) {
			if (data != null && data.getIntExtra(ExtraAttr.POPUP_RESULT, StatusCode.FAILED.status) == StatusCode.SUCCESS.status)
				finish()
		} else {
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
	}

	fun setupLabels() {
		val sql = """
			SELECT _ID, LABEL_TITLE, LABEL_COLOR FROM LABEL_TB
		""".trimIndent()

		radioGroup = MyRadioGroup()
		radioGroup.setOnChangeListener(LabelSelectedListener)

		val result = if (UserData.careUser == null) ServerHandler.send(this@EditAlarmActivity, EndOfAPI.GET_LABELS)
		else ServerHandler.send(this@EditAlarmActivity, EndOfAPI.SYNC_GET_LABELS, id=UserData.careUser)

		if (!HttpHelper.isOK(result)) {
			Toast.makeText(this@EditAlarmActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
			finish()
		}

		val lLabels = preprocess(result["data"].asJsonArray)

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

	fun preprocess(lLabels: JsonArray): JsonArray {
		val none = lLabels[0].asJsonObject
		val defaultId = none["labelId"].asInt
		none.addProperty("title", "None")
		lLabels.set(0, none)

		for (i in 1..3) {
			val label = lLabels[i].asJsonObject
			when (label["labelId"].asInt) {
				defaultId + 1 -> {
					if (label["title"].asString == "감기") {
						label.addProperty("title", "Cold")
						lLabels.set(i, label)
					}
				}
				defaultId + 2 -> {
					if (label["title"].asString == "비염") {
						label.addProperty("title", "Rhinitis")
						lLabels.set(i, label)
					}
				}
				defaultId + 3 -> {
					if (label["title"].asString == "알레르기") {
						label.addProperty("title", "Allergy")
						lLabels.set(i, label)
					}
				}
			}
		}
		return lLabels
	}

	val LabelSelectedListener = object : MyRadioGroup.OnChangeListener {
		override fun onChange(after: RadioButton) {
			labelSelected = radioGroup.getCheckedTag()
		}
	}

	inner class EditTimeRecyclerAdapter(val lTimes: JsonArray) :
		RecyclerView.Adapter<EditTimeRecyclerAdapter.ViewHolder>() {
		val curTimes = mutableListOf<String>()

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
			val sTime = lTimes[position].asString
			if (curTimes.size < lTimes.size()) curTimes.add(sTime)
			holder.time.text = sTime
			holder.time.setOnClickListener {
				val cal = Calendar.getInstance()
				cal.time = SDF.timeInKorean.parse(holder.time.text.toString())
				TimePickerDialog(this@EditAlarmActivity, { timePicker: TimePicker, hourOfDay: Int, minute: Int ->
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
			val view = LayoutInflater.from(this@EditAlarmActivity).inflate(R.layout.alarm_page_photo_item, parent, false)
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