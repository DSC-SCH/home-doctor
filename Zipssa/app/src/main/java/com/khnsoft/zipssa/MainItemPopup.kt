package com.khnsoft.zipssa

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.list_item_popup.*
import java.text.SimpleDateFormat
import java.util.*

class MainItemPopup : AppCompatActivity() {
    val sdf_date_save = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    val sdf_date_show = SimpleDateFormat("M'월' d'일'", Locale.KOREA)
    val sdf_time_save = SimpleDateFormat("HH:mm", Locale.KOREA)
    val sdf_time_show = SimpleDateFormat("a h:mm", Locale.KOREA)

    lateinit var animOpen: Animation
    lateinit var animClose: Animation
    lateinit var layoutAnimOpen: Animation
    lateinit var layoutAnimClose: Animation
    var isOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_item_popup)
        setupFloatingPosition()

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
        animOpen = AnimationUtils.loadAnimation(this@MainItemPopup, R.anim.floating_open)
        animClose = AnimationUtils.loadAnimation(this@MainItemPopup, R.anim.floating_close)
        layoutAnimOpen = AnimationUtils.loadAnimation(this@MainItemPopup, R.anim.floating_layout_open)
        layoutAnimClose = AnimationUtils.loadAnimation(this@MainItemPopup, R.anim.floating_layout_close)

        pause_btn.setOnClickListener {
            if (!isOpened)
                anim()
        }

        floating_layout.setOnClickListener {
            if (isOpened)
                anim()
        }

        cancel_oneday.setOnClickListener {
            // TODO("Cancel one day alarm")
            val data = MyAlertPopup.Data(AlertType.CONFIRM).apply {
                alertTitle = jItem["alarm_title"].asString
                alertContent = "해당 알림이 당일만 중단되었습니다."
            }
            val dataId = DataPasser.insert(data)
			val intent = Intent(this@MainItemPopup, MyAlertPopup::class.java)
			intent.putExtra(MyAlertPopup.EXTRA_DATA, dataId)
			startActivity(intent)
        }

        cancel_future.setOnClickListener {
            val calYesterday = Calendar.getInstance()
            calYesterday.add(Calendar.DAY_OF_MONTH, -1)
            val json = JsonObject()
            json.addProperty("alarm_end_date", sdf_date_save.format(calYesterday))
            ServerHandler.send(EndOfAPI.EDIT_ALARM, json.toString(), jItem["ALARM_ID"].asInt)

			val data = MyAlertPopup.Data(AlertType.CONFIRM).apply {
				alertTitle = jItem["alarm_title"].asString
				alertContent = "이후 모든 알림이 중단되었습니다."
			}
			val dataId = DataPasser.insert(data)
			val intent = Intent(this@MainItemPopup, MyAlertPopup::class.java)
			intent.putExtra(MyAlertPopup.EXTRA_DATA, dataId)
			startActivity(intent)
        }

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

    fun anim() {
        if (isOpened) {
            floating_container.startAnimation(animClose)
            floating_layout.startAnimation(layoutAnimClose)
        } else {
            floating_container.startAnimation(animOpen)
            floating_layout.startAnimation(layoutAnimOpen)
        }
        isOpened = !isOpened
    }

    fun setupFloatingPosition() {
        floating_container.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        alarm_label.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val titleSizeV = alarm_label.measuredHeight
        val scrollSizeV = resources.getDimension(R.dimen.main_popup_scroll_sizeV)
        val buttonsPaddingH = resources.getDimension(R.dimen.main_popup_buttons_paddingH)

        val marginL = buttonsPaddingH
        val marginT = titleSizeV + scrollSizeV - floating_container.measuredHeight

        val floatingParams = floating_container.layoutParams as LinearLayout.LayoutParams
        floatingParams.leftMargin = marginL.toInt()
        floatingParams.topMargin = marginT.toInt()
        floating_container.layoutParams = floatingParams
    }
}