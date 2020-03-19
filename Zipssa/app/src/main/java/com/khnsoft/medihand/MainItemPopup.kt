package com.khnsoft.medihand

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
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
        val _selectedDate = intent.getStringExtra(ExtraAttr.SELECTED_DATE)
        val _count = intent.getIntExtra(ExtraAttr.SELECTED_COUNT, Int.MAX_VALUE)
        val result = if (UserData.careUser == null) ServerHandler.send(this@MainItemPopup, EndOfAPI.GET_ALARM, id=_id)
        else ServerHandler.send(this@MainItemPopup, EndOfAPI.SYNC_GET_ALARM, id=UserData.careUser, id2=_id)

        if (!HttpHelper.isOK(result)) {
            Toast.makeText(this@MainItemPopup, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
            finish()
        }
        val jTemp = result["data"].asJsonObject

        val jItem = ServerHandler.convertKeys(jTemp, ServerHandler.alarmToLocal)

        val drawable = alarm_label.background as GradientDrawable
        drawable.setColor(Color.parseColor("${jItem["label_color"].asString}"))

        alarm_title.text = jItem["alarm_title"].asString
        alarm_switch.isChecked = AlarmParser.parseStatus(jItem["alarm_enabled"].asString) == AlarmStatus.ENABLE
        start_date.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        start_date.text = SDF.monthDateInKorean.format(SDF.dateBar.parse(jItem["alarm_start_date"].asString))
        end_date.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        end_date.text = SDF.monthDateInKorean.format(SDF.dateBar.parse(jItem["alarm_end_date"].asString))

        val lTimes = AlarmParser.parseTimes(jItem["alarm_times"].asString)
        alarm_count.text = "하루 ${lTimes.size()}회 복용"

        // edit count start
        alarm_count.setOnClickListener {
            if (UserData.accountType == AccountType.NO_NETWORK) return@setOnClickListener
            val selectedDate = SDF.dateSlash.parse(_selectedDate)
            val curTime = System.currentTimeMillis()
            val pastTime = curTime - selectedDate.time
            val timePast = AlarmHandler.getPastAlarmTime(jItem, curTime)
            val timeNext = AlarmHandler.getNextAlarmTime(jItem, curTime)
            var lastIsNow = false
            val date: Int

            if (pastTime > DateUtils.DAY_IN_MILLIS) {
                date = -1
            } else if (0 < pastTime && pastTime < DateUtils.DAY_IN_MILLIS) {
                date = 0
            } else {
                date = 1
            }

            if (timePast < curTime && curTime < timeNext) {
                lastIsNow = true
            }

            val passCount: Int
            if (date == -1) {
                passCount = lTimes.size() - _count
            } else if (date == 0) {
                var _temp = 0
                for (time in lTimes) {
                    if (SDF.dateTime.parse("${SDF.dateBar.format(selectedDate)} ${time.asString}").time <= timePast) _temp++
                }
                passCount = _temp - _count
            } else {
                passCount = 0
            }

            if (passCount <= 0) {
                val data = MyAlertPopup.Data(AlertType.CONFIRM).apply {
                    alertTitle = "축하드립니다"
                    alertContent = "지나친 알람이 없습니다."
                }
                val dataId = DataPasser.insert(data)
                val intent = Intent(this@MainItemPopup, MyAlertPopup::class.java)
                intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
                startActivity(intent)
                return@setOnClickListener
            }

            val alarmId = jItem["alarm_id"].asInt
            val data = MyAlertPopup.Data(AlertType.ALERT).apply {
                alertTitle = "지나친 알람이 ${passCount}회 있습니다"
                alertContent = "1회 복용 표시하시겠습니까?"
                alertConfirmText = "확인"
                confirmListener = View.OnClickListener {
                    val json = JsonObject()
                    json.addProperty("date", SDF.dateBar.format(selectedDate))
                    json.addProperty("count", 1)
                    val result = ServerHandler.send(this@MainItemPopup, EndOfAPI.PUT_COUNT_ALARM, json, alarmId)
                    if (!HttpHelper.isOK(result)) {
                        Toast.makeText(this@MainItemPopup, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }
                    if (passCount == 1 && lastIsNow) {
                        AlarmHandler.clearAlarmById(this@MainItemPopup, alarmId)
                        AlarmHandler.createAlarm(this@MainItemPopup, jItem)
                    }
                }
            }

            val dataId = DataPasser.insert(data)
            val intent = Intent(this@MainItemPopup, MyAlertPopup::class.java)
            intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
            startActivityForResult(intent, MyAlertPopup.RC)
        }

        // edit count end

        var timeBox: MainRemainTimeItem
        var timeView: TextView
        for (item in lTimes) {
            timeBox = MainRemainTimeItem(this@MainItemPopup)
            times_container.addView(timeBox)
            timeView = findViewById(R.id.time_item)
            timeView.id = 0
            timeView.text = SDF.timeInKorean.format(SDF.time.parse(item.asString))
        }

        if (UserData.careUser != null)
            alarm_switch.isEnabled = false
        alarm_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (UserData.accountType == AccountType.NO_NETWORK) {
                MyAlertPopup.needNetwork(this@MainItemPopup)
                return@setOnCheckedChangeListener
            }
            val json = JsonObject()
            json.addProperty("alarm_enabled", if (isChecked) AlarmStatus.ENABLE.name else AlarmStatus.CANCEL.name)

            val result = ServerHandler.send(this@MainItemPopup, EndOfAPI.CHANGE_ALARM_STATE, json, _id)

            if (!HttpHelper.isOK(result)) {
                Toast.makeText(this@MainItemPopup, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
                alarm_switch.isChecked = !isChecked
            }

            jItem.addProperty("alarm_enabled", if (isChecked) AlarmStatus.ENABLE.name else AlarmStatus.CANCEL.name)
            AlarmHandler.clearAlarmById(this@MainItemPopup, _id)
            AlarmHandler.createAlarm(this@MainItemPopup, jItem)
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
            val sp = SPHandler.getSp(this@MainItemPopup)
            val editor = sp.edit()
            val stopUntil = sp.getLong(AlarmReceiver.SP_STOP_UNTIL, -1)
            val stopIds: JsonArray
            if (stopUntil > System.currentTimeMillis()) {
                stopIds = JsonParser.parseString(sp.getString(AlarmReceiver.SP_STOP_ID, "[]")).asJsonArray
            } else {
                stopIds = JsonArray()
                editor.putLong(AlarmReceiver.SP_STOP_UNTIL, SDF.dateBar.parse(SDF.dateBar.format(Date())).time + DateUtils.DAY_IN_MILLIS)
                editor.apply()
            }
            var isExists = false
            for (stopId in stopIds) {
                if (stopId.asInt == _id) {
                    isExists = true
                }
            }
            if (!isExists) {
                stopIds.add(_id)
                editor.putString(AlarmReceiver.SP_STOP_ID, stopIds.toString())
                editor.commit()
            }

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
            if (UserData.accountType == AccountType.NO_NETWORK) {
                MyAlertPopup.needNetwork(this@MainItemPopup)
                return@setOnClickListener
            }

            val calYesterday = Calendar.getInstance()
            calYesterday.add(Calendar.DAY_OF_MONTH, -1)
            jItem.addProperty("alarm_end_date", SDF.dateBar.format(calYesterday.time))
            val result = if (UserData.careUser == null) ServerHandler.send(this@MainItemPopup, EndOfAPI.EDIT_ALARM, jItem, _id)
            else ServerHandler.send(this@MainItemPopup, EndOfAPI.SYNC_EDIT_ALARM, jItem, UserData.careUser, _id)

            if (!HttpHelper.isOK(result)) {
                Toast.makeText(this@MainItemPopup, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (UserData.careUser == null) {
                AlarmHandler.clearAlarmById(this@MainItemPopup, _id)
                AlarmHandler.createAlarm(this@MainItemPopup, jItem)
            }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MyAlertPopup.RC) {
            if (data != null && data.getIntExtra(ExtraAttr.POPUP_RESULT, StatusCode.FAILED.status) == StatusCode.SUCCESS.status)
                finish()
        }
    }
}