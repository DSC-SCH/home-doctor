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
import kotlinx.android.synthetic.main.list_item_popup.*
import java.util.*

class MainItemPopup : AppCompatActivity() {

    lateinit var animOpen: Animation
    lateinit var animClose: Animation
    lateinit var layoutAnimOpen: Animation
    lateinit var layoutAnimClose: Animation
    var isOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_item_popup)
        setupFloatingPosition()

        val _id = intent.getIntExtra(ExtraAttr.ALARM_ID, -1)
        val jTemp = if (UserData.careUser == null) ServerHandler.send(this@MainItemPopup, EndOfAPI.GET_ALARM, id=_id)["data"].asJsonObject
        else ServerHandler.send(this@MainItemPopup, EndOfAPI.SYNC_GET_ALARM, id=UserData.careUser, id2=_id)
        val jItem = ServerHandler.convertKeys(jTemp, ServerHandler.alarmToLocal)

        val drawable = alarm_label.background as GradientDrawable
        drawable.setColor(Color.parseColor("${jItem["label_color"].asString}"))

        alarm_title.text = jItem["alarm_title"].asString
        alarm_switch.isChecked = AlarmParser.parseStatus(jItem["alarm_enabled"].asString) == AlarmStatus.ENABLED
        start_date.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        start_date.text = SDF.monthDateInKorean.format(SDF.dateBar.parse(jItem["alarm_start_date"].asString))
        end_date.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        end_date.text = SDF.monthDateInKorean.format(SDF.dateBar.parse(jItem["alarm_end_date"].asString))

        val jTimes = AlarmParser.parseTimes(jItem["alarm_times"].asString)
        alarm_count.text = "${jTimes.size()}"

        var timeBox: MainRemainTimeItem
        var timeView: TextView
        for (item in jTimes) {
            timeBox = MainRemainTimeItem(this@MainItemPopup)
            times_container.addView(timeBox)
            timeView = findViewById(R.id.time_item)
            timeView.id = 0
            timeView.text = SDF.timeInKorean.format(SDF.time.parse(item.asString))
        }

        if (UserData.careUser != null)
            alarm_switch.isEnabled = false
        alarm_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            val json = JsonObject()
            json.addProperty("alarm_enabled", if (isChecked) AlarmStatus.ENABLED.status else AlarmStatus.DISABLED.status)

            val result = ServerHandler.send(this@MainItemPopup, EndOfAPI.CHANGE_ALARM_STATE, json, _id)

            if (!HttpHelper.isOK(result))
                alarm_switch.isChecked = false
        }

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
			intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
			startActivity(intent)
        }

        cancel_future.setOnClickListener {
            val calYesterday = Calendar.getInstance()
            calYesterday.add(Calendar.DAY_OF_MONTH, -1)
            val json = JsonObject()
            json.addProperty("alarm_end_date", SDF.dateBar.format(calYesterday))
            val result = if (UserData.careUser == null) ServerHandler.send(this@MainItemPopup, EndOfAPI.EDIT_ALARM, json, _id)
            else ServerHandler.send(this@MainItemPopup, EndOfAPI.SYNC_EDIT_ALARM, json, UserData.careUser, _id)

            if (HttpHelper.isOK(result)) {
                val data = MyAlertPopup.Data(AlertType.CONFIRM).apply {
                    alertTitle = jItem["alarm_title"].asString
                    alertContent = "이후 모든 알림이 중단되었습니다."
                }
                val dataId = DataPasser.insert(data)
                val intent = Intent(this@MainItemPopup, MyAlertPopup::class.java)
                intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
                startActivity(intent)
                finish()
            }
        }

        confirm_btn.setOnClickListener {
            finish()
        }

        edit_btn.setOnClickListener {
            val intent = Intent(this@MainItemPopup, EditAlarmActivity::class.java)
            intent.putExtra(ExtraAttr.ALARM_ID, _id)
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