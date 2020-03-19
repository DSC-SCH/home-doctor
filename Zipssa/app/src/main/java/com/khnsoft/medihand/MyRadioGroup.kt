package com.khnsoft.medihand

import android.view.View
import android.widget.RadioButton

class MyRadioGroup {
	val radios = MutableList(0) {RadioButton(null)}
	var mOnChangeListener: OnChangeListener? = null

	fun add(rb: RadioButton) {
		radios.add(rb)
		rb.setOnClickListener(onClick)
	}

	val onClick = View.OnClickListener{
		for (rb in radios) {
			rb.isChecked = false
		}

		(it as RadioButton).isChecked = true

		mOnChangeListener?.onChange(it)
	}

	fun getCheckedIndex(): Int {
		for (i in 0..radios.size-1) {
			if (radios[i].isChecked) return i
		}
		return -1
	}

	fun getCheckedTag(): Int {
		for (rb in radios) {
			if (rb.isChecked) return rb.tag as Int
		}
		return -1
	}

	fun getButtonByTag(tag: Int) : RadioButton? {
		for (rb in radios) {
			if ((rb.tag as Int) == tag) return rb
		}
		return null
	}

	fun setOnChangeListener(onChangeListener: OnChangeListener) {
		mOnChangeListener = onChangeListener
	}

	interface OnChangeListener {
		fun onChange(after: RadioButton)
	}
}