package com.khnsoft.zipssa

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.mypage_history_activity.*

class MypageHistoryActivity : AppCompatActivity() {
	companion object {
		const val PAGE_ALL = 0
		const val PAGE_PHOTO = 1
	}

	var curPage = PAGE_ALL

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_history_activity)

		back_btn.setOnClickListener { onBackPressed() }

		val radioGroup = MyRadioGroup()
		radioGroup.add(history_all)
		radioGroup.add(history_photo)

		radioGroup.setOnChangeListener(object: MyRadioGroup.OnChangeListener {
			override fun onChange(after: RadioButton) {
				changePage(if (after.id == R.id.history_photo) PAGE_PHOTO else PAGE_ALL)
			}
		})

		changePage(curPage)
	}

	fun changePage(page: Int) {
		curPage = page
		refresh()
	}

	fun refresh() {
		when (curPage) {
			PAGE_ALL -> {
				val lHistory = ServerHandler.send(this@MypageHistoryActivity, EndOfAPI.GET_ALL_ALARMS)["data"].asJsonArray

				val lm = LinearLayoutManager(this@MypageHistoryActivity)
				val adapter = HistoryRecyclerAdapter(lHistory)
				history_container.layoutManager = lm
				history_container.adapter = adapter
			}
			PAGE_PHOTO -> {
				val lPhoto = ServerHandler.send(this@MypageHistoryActivity, EndOfAPI.GET_ALL_IMAGES)["data"].asJsonArray

				val lm = GridLayoutManager(this@MypageHistoryActivity, 2)
				val adapter = PhotoRecyclerAdapter(lPhoto)
				history_container.layoutManager = lm
				history_container.adapter = adapter
			}
		}
	}

	override fun onResume() {
		super.onResume()
		refresh()
	}

	inner class HistoryRecyclerAdapter(val lHistory: JsonArray) :
		RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val alarm_title = itemView.findViewById<TextView>(R.id.alarm_title)
			val start_date = itemView.findViewById<TextView>(R.id.start_date)
			val end_date = itemView.findViewById<TextView>(R.id.end_date)
			val alarm_count = itemView.findViewById<TextView>(R.id.alarm_count)
			val created_date = itemView.findViewById<TextView>(R.id.created_date)
			val container = itemView.findViewById<LinearLayout>(R.id.history_item)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(this@MypageHistoryActivity).inflate(R.layout.mypage_history_all_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lHistory.size()
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			val jItem = ServerHandler.convertKeys(lHistory[position].asJsonObject, ServerHandler.alarmToLocal)

			holder.alarm_title.text = jItem["alarm_title"].asString
			holder.alarm_title.setBackgroundColor(Color.parseColor(jItem["label_color"].asString))
			holder.start_date.text = SDF.dateDotShort.format(SDF.dateBar.parse(jItem["alarm_start_date"].asString))
			holder.end_date.text = SDF.dateDotShort.format(SDF.dateBar.parse(jItem["alarm_end_date"].asString))
			holder.alarm_count.text = AlarmParser.parseTimes(jItem["alarm_times"].asString).size().toString()
			holder.created_date.text = SDF.dateDot.format(SDF.dateBar.parse(jItem["created_date"].asString))
			holder.container.setOnClickListener {
				val intent = Intent(this@MypageHistoryActivity, MainItemPopup::class.java)
				intent.putExtra(ExtraAttr.ALARM_ID, jItem["alarm_id"].asInt)
				startActivity(intent)
			}
		}
	}

	inner class PhotoRecyclerAdapter(val lPhoto: JsonArray) :
		RecyclerView.Adapter<PhotoRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val container = itemView.findViewById<LinearLayout>(R.id.image_container)
			val image = itemView.findViewById<ImageView>(R.id.image_item)
			val title = itemView.findViewById<TextView>(R.id.alarm_title)
			val created_date = itemView.findViewById<TextView>(R.id.created_date)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(this@MypageHistoryActivity).inflate(R.layout.mypage_history_photo_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lPhoto.size()
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			val jItem = ServerHandler.convertKeys(lPhoto[position].asJsonObject, ServerHandler.imageToLocal)

			holder.title.text = jItem["alarm_title"].asString
			holder.title.setBackgroundColor(Color.parseColor(jItem["label_color"].asString))
			holder.image.setImageBitmap(ImageHelper.base64ToBitmap(jItem["image"].asString))
			holder.created_date.text = SDF.dateDot.format(SDF.dateBar.parse(jItem["created_date"].asString))
			holder.container.setOnClickListener {
				val intent = Intent(this@MypageHistoryActivity, MypageHistoryPhotoDetailActivity::class.java)
				intent.putExtra(ExtraAttr.CUR_PHOTO, position)
				startActivity(intent)
			}
		}
	}
}
