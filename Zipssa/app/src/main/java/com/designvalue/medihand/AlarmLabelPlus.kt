package com.designvalue.medihand

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout

class AlarmLabelPlus(context: Context) : LinearLayout(context) {
	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		inflater.inflate(R.layout.label_plus_item, this, true)
	}
}