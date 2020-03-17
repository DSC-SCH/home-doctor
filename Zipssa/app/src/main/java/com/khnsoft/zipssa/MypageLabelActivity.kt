package com.khnsoft.zipssa

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.mypage_label_activity.*

class MypageLabelActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_label_activity)

		back_btn.setOnClickListener { onBackPressed() }

		add_label_btn.setOnClickListener {
			val intent = Intent(this@MypageLabelActivity, AddLabelPopup::class.java)
			startActivity(intent)
		}
	}

	override fun onResume() {
		super.onResume()
		refresh()
	}

	fun refresh() {
		val result = ServerHandler.send(this@MypageLabelActivity, EndOfAPI.GET_LABELS)

		if (!HttpHelper.isOK(result)) {
			Toast.makeText(this@MypageLabelActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
			return
		}
		val lLabels = result["data"].asJsonArray

		val adapter = LabelRecyclerAdapter(lLabels)
		val lm = LinearLayoutManager(this@MypageLabelActivity)
		label_container.layoutManager = lm
		label_container.adapter = adapter
	}

	inner class LabelRecyclerAdapter(val lLabels: JsonArray) :
			RecyclerView.Adapter<LabelRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val color = itemView.findViewById<View>(R.id.label_color)
			val title = itemView.findViewById<TextView>(R.id.label_title)
			val edit = itemView.findViewById<TextView>(R.id.label_edit)
			val delete = itemView.findViewById<TextView>(R.id.label_delete)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(this@MypageLabelActivity).inflate(R.layout.mypage_label_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lLabels.size()
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			val jItem = ServerHandler.convertKeys(lLabels[position].asJsonObject, ServerHandler.labelToLocal)

			val drawable = holder.color.background as GradientDrawable
			drawable.setColor(Color.parseColor(jItem["label_color"].asString))
			holder.title.text = jItem["label_title"].asString
			if (position == 0) {
				holder.edit.visibility = View.GONE
				holder.delete.visibility = View.GONE
			}

			holder.edit.setOnClickListener {
				val intent = Intent(this@MypageLabelActivity, EditLabelPopup::class.java)
				intent.putExtra(ExtraAttr.LABEL, jItem.toString())
				startActivity(intent)
			}

			holder.delete.setOnClickListener {
				val data = MyAlertPopup.Data(AlertType.ALERT).apply {
					alertTitle = jItem["label_title"].asString
					alertContent = "해당 라벨을 삭제하시겠습니까?"
					alertConfirmText = "삭제"
					confirmListener = View.OnClickListener {
						val result = ServerHandler.send(this@MypageLabelActivity,EndOfAPI.DELETE_LABEL, id=jItem["label_id"].asInt)
						if (!HttpHelper.isOK(result)) {
							Toast.makeText(this@MypageLabelActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
							return@OnClickListener
						}

						val data = MyAlertPopup.Data(AlertType.CONFIRM)
						data.alertTitle = alertTitle
						data.alertContent = "해당 라벨이 삭제되었습니다."
						val dataId = DataPasser.insert(data)

						val intent = Intent(this@MypageLabelActivity, MyAlertPopup::class.java)
						intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
						startActivity(intent)
					}
				}

				val dataId = DataPasser.insert(data)
				val intent = Intent(this@MypageLabelActivity, MyAlertPopup::class.java)
				intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
				startActivityForResult(intent, MyAlertPopup.RC)
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == MyAlertPopup.RC) {
			if (data != null && data.getIntExtra(ExtraAttr.POPUP_RESULT, StatusCode.FAILED.status) == StatusCode.SUCCESS.status)
				refresh()
		}
	}
}
