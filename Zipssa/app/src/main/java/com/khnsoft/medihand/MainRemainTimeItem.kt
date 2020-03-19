package com.khnsoft.medihand

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout

class MainRemainTimeItem(context: Context) : LinearLayout(context) {
	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		inflater.inflate(R.layout.list_remain_time_item, this, true)
	}
}