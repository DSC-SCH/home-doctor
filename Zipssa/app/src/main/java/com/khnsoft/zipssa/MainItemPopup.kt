package com.khnsoft.zipssa

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.list_item_popup.*
import java.text.SimpleDateFormat
import java.util.*

class MainItemPopup : AppCompatActivity() {
	val sdf_date_save = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
	val sdf_date_show = SimpleDateFormat("M'월' d'일'", Locale.KOREA)
	val sdf_time_save = SimpleDateFormat("HH:mm", Locale.KOREA)
	val sdf_time_show = SimpleDateFormat("a h:mm", Locale.KOREA)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.list_item_popup)

		val sItem = intent.getStringExtra("jItem")
		val jItem = JsonParser.parseString(sItem).asJsonObject

		val drawable = alarm_label.background as GradientDrawable
		drawable.setColor(Color.parseColor("${jItem["LABEL_COLOR"].asString}"))

		alarm_title.text = jItem["ALARM_TITLE"].asString
		alarm_switch.isChecked = (jItem["ALARM_ENABLED"].asInt == 1)
		start_date.paintFlags = Paint.UNDERLINE_TEXT_FLAG
		start_date.text = sdf_date_show.format(sdf_date_save.parse(jItem["ALARM_START_DT"].asString))
		end_date.paintFlags = Paint.UNDERLINE_TEXT_FLAG
		end_date.text = sdf_date_show.format(sdf_date_save.parse(jItem["ALARM_END_DT"].asString))

		val jTimes = JsonParser.parseString(jItem["ALARM_TIMES"].asString).asJsonArray
		alarm_count.text = "${jTimes.size()}"

		var timeBox: MainRemainTimeItem
		var timeView: TextView
		for (item in jTimes) {
			timeBox = MainRemainTimeItem(this@MainItemPopup)
			times_container.addView(timeBox)
			timeView = findViewById(R.id.time_item)
			timeView.id = 0
			timeView.text = sdf_time_show.format(sdf_time_save.parse(item.asString))
		}

		// TODO("Abort since current date")

		confirm_btn.setOnClickListener {
			finish()
		}

		edit_btn.setOnClickListener {
			val intent = Intent(this@MainItemPopup, EditAlarmActivity::class.java)
			intent.putExtra("jItem", sItem)
			startActivity(intent)
			finish()
		}
	}
}