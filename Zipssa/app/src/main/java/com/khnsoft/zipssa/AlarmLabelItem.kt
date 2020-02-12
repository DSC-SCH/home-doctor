package com.khnsoft.zipssa

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout

class AlarmLabelItem(context: Context) : LinearLayout(context) {
	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		inflater.inflate(R.layout.alarm_label_item, this, true)
	}
}