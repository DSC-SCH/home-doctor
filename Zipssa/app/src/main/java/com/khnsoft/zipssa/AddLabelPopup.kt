package com.khnsoft.zipssa

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableContainer
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.add_label_popup.*

class AddLabelPopup : AppCompatActivity() {
	val LABELS_COLORS = listOf<String>(
		"#FF7070", "#FFD4C4", "#FFFFBE", "#CAFFA7", "#6FD472",
		"#A9E6DA", "#74B9F2", "#B1E1FE", "#919CD4", "#E3DAF9",
		"#DFBBE9", "#F194DE", "#FFE2F7", "#D4D4D4", "#B29090")

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.add_label_popup)

		// Setting labels
		val radioGroup = MyRadioGroup()
		var radioBtn: RadioButton
		var drawable: StateListDrawable
		var drawableState: DrawableContainer.DrawableContainerState
		var children: Array<Drawable>
		var selected: GradientDrawable
		var unselected: GradientDrawable

		for (i in 0..14) {
			radioBtn = findViewById<RadioButton>(resources.getIdentifier("label_${i+1}", "id", packageName))
			radioGroup.add(radioBtn)
			drawable = radioBtn.background as StateListDrawable
			drawableState = drawable.constantState as DrawableContainer.DrawableContainerState
			children = drawableState.children
			selected = children[0] as GradientDrawable
			selected.setColor(Color.parseColor(LABELS_COLORS[i]))
			unselected = children[1] as GradientDrawable
			unselected.setColor(Color.parseColor(LABELS_COLORS[i]))
		}

		add_label_btn.setOnClickListener {
			if (label_title.text.isBlank()) {
				Toast.makeText(this@AddLabelPopup, "증상을 입력해 주세요.", Toast.LENGTH_LONG).show()
				return@setOnClickListener
			}

			val colorIndex = radioGroup.getCheckedIndex()
			if (colorIndex == -1) {
				Toast.makeText(this@AddLabelPopup, "색상을 선택해 주세요.", Toast.LENGTH_LONG).show()
				return@setOnClickListener
			}

			val sql = """
				|INSERT INTO LABEL_TB (LABEL_TITLE, LABEL_COLOR) 
				|VALUES ("${label_title.text}", "${LABELS_COLORS[radioGroup.getCheckedIndex()]}")
			""".trimMargin()
			val mHandler = DBHandler.open(this@AddLabelPopup)
			if (mHandler.execNonResult(sql) == 0) {
				mHandler.close()
				finish()
			}
			mHandler.close()
		}
	}
}