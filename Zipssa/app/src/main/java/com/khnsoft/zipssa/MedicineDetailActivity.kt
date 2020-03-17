package com.khnsoft.zipssa

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.medicine_detail_activity.*

class MedicineDetailActivity : AppCompatActivity() {
	var isEffectExpanded = false
	var isDosageExpanded = false
	var isValidityExpanded = false
	var isPreservationExpanded = false
	var isWarningExpanded = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.medicine_detail_activity)

		val jItem = JsonParser.parseString(intent.getStringExtra(ExtraAttr.MEDICINE)).asJsonObject

		medicine_name.text = jItem["name"].asString
		medicine_effect.text = jItem["effect"].asString
		medicine_dosage.text = jItem["dosage"].asString
		medicine_validity.text = jItem["validDate"].asString
		medicine_preservation.text = jItem["saveMethod"].asString
		medicine_warning.text = jItem["precaution"].asString

		effect_btn.setOnClickListener {
			if (isEffectExpanded) {
				effect_icon.setImageResource(R.drawable.arrow_down)
				effect_content.visibility = View.GONE
			} else {
				effect_icon.setImageResource(R.drawable.arrow_up)
				effect_content.visibility = View.VISIBLE
			}

			isEffectExpanded = !isEffectExpanded
		}

		dosage_btn.setOnClickListener {
			if (isDosageExpanded) {
				dosage_icon.setImageResource(R.drawable.arrow_down)
				dosage_content.visibility = View.GONE
			} else {
				dosage_icon.setImageResource(R.drawable.arrow_up)
				dosage_content.visibility = View.VISIBLE
			}

			isDosageExpanded = !isDosageExpanded
		}

		validity_btn.setOnClickListener {
			if (isValidityExpanded) {
				validity_icon.setImageResource(R.drawable.arrow_down)
				validity_content.visibility = View.GONE
			} else {
				validity_icon.setImageResource(R.drawable.arrow_up)
				validity_content.visibility = View.VISIBLE
			}

			isValidityExpanded = !isValidityExpanded
		}

		preservation_btn.setOnClickListener {
			if (isPreservationExpanded) {
				preservation_icon.setImageResource(R.drawable.arrow_down)
				preservation_content.visibility = View.GONE
			} else {
				preservation_icon.setImageResource(R.drawable.arrow_up)
				preservation_content.visibility = View.VISIBLE
			}

			isPreservationExpanded = !isPreservationExpanded
		}

		warning_btn.setOnClickListener {
			if (isWarningExpanded) {
				warning_icon.setImageResource(R.drawable.arrow_down)
				warning_content.visibility = View.GONE
			} else {
				warning_icon.setImageResource(R.drawable.arrow_up)
				warning_content.visibility = View.VISIBLE
			}

			isWarningExpanded = !isWarningExpanded
		}
	}
}