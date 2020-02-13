package com.khnsoft.zipssa

import android.view.View
import android.widget.RadioButton

class MyRadioGroup {
	val radios = MutableList(0) {RadioButton(null)}

	constructor()

	constructor(vararg rbs:RadioButton) {
		for (rb in rbs) {
			radios.add(rb)
			rb.setOnClickListener(onClick)
		}
	}

	fun add(rb: RadioButton) {
		radios.add(rb)
		rb.setOnClickListener(onClick)
	}

	val onClick = View.OnClickListener{
		for (rb in radios) {
			rb.isChecked = false
		}

		(it as RadioButton).isChecked = true
	}

	fun getCheckedIndex(): Int {
		for (i in 0..radios.size-1) {
			if (radios[i].isChecked) return i
		}
		return -1
	}

	fun getLastButton(): RadioButton {
		return radios[radios.size-1]
	}
}