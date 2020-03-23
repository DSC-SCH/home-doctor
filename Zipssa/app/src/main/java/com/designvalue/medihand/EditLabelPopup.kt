package com.designvalue.medihand

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableContainer
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.edit_label_popup.*
import java.util.*

class EditLabelPopup : AppCompatActivity() {

	val LABELS_COLORS = listOf<String>(
		"#FF7070", "#FFD4C4", "#FFFFBE", "#CAFFA7", "#6FD472",
		"#A9E6DA", "#74B9F2", "#B1E1FE", "#919CD4", "#E3DAF9",
		"#DFBBE9", "#F194DE", "#FFE2F7", "#D4D4D4", "#B29090")


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.edit_label_popup)

		val _jItem = JsonParser.parseString(intent.getStringExtra(ExtraAttr.LABEL)).asJsonObject

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

			if (LABELS_COLORS[i] == _jItem["label_color"].asString)
				radioBtn.callOnClick()
		}

		edit_label_btn.setOnClickListener {
			if (label_title.text.isBlank()) {
				Toast.makeText(this@EditLabelPopup, "증상을 입력해 주세요.", Toast.LENGTH_LONG).show()
				return@setOnClickListener
			}

			val colorIndex = radioGroup.getCheckedIndex()
			if (colorIndex == -1) {
				Toast.makeText(this@EditLabelPopup, "색상을 선택해 주세요.", Toast.LENGTH_LONG).show()
				return@setOnClickListener
			}

			val data = MyAlertPopup.Data(AlertType.ALERT).apply {
				alertTitle = label_title.text.toString()
				alertContent = "해당 라벨 정보를 수정하시겠습니까?"
				alertConfirmText = "수정"
				confirmListener = View.OnClickListener {
					val curCal = Calendar.getInstance()
					val json = JsonObject()
					json.addProperty("label_title", label_title.text.toString())
					json.addProperty("label_color", LABELS_COLORS[radioGroup.getCheckedIndex()])

					val result = ServerHandler.send(this@EditLabelPopup, EndOfAPI.EDIT_LABEL, json, _jItem["label_id"].asInt)
					if (!HttpHelper.isOK(result)) {
						Toast.makeText(this@EditLabelPopup, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
						return@OnClickListener
					}

					val data = MyAlertPopup.Data(AlertType.CONFIRM)
					data.alertTitle = alertTitle
					data.alertContent = "해당 라벨이 수정되었습니다."
					val dataId = DataPasser.insert(data)

					val intent = Intent(this@EditLabelPopup, MyAlertPopup::class.java)
					intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
					startActivity(intent)
				}
			}

			val dataId = DataPasser.insert(data)
			val intent = Intent(this@EditLabelPopup, MyAlertPopup::class.java)
			intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
			startActivityForResult(intent, MyAlertPopup.RC)
		}

		label_title.setText(_jItem["label_title"].asString)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == MyAlertPopup.RC) {
			if (data != null && data.getIntExtra(ExtraAttr.POPUP_RESULT, StatusCode.FAILED.status) == StatusCode.SUCCESS.status)
				finish()
		}
	}
}