package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.mypage_history_activity.*

class MypageHistoryActivity : AppCompatActivity() {
	val PAGE_ALL = 0
	val PAGE_PHOTO = 1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_history_activity)

		back_btn.setOnClickListener { onBackPressed() }

		val radioGroup = MyRadioGroup()
		radioGroup.add(history_all)
		radioGroup.add(history_photo)
	}

	fun changePage(page: Int) {
		when (page) {
			PAGE_ALL -> {
				// TODO("Get history from API")
				val lHistory = JsonArray()

				val lm = LinearLayoutManager(this@MypageHistoryActivity)
				val adapter = HistoryRecyclerAdapter(lHistory)
				history_container.layoutManager = lm
				history_container.adapter = adapter
			}
			PAGE_PHOTO -> {
				// TODO("Get photo from API")
				val lPhoto = JsonArray()

				val lm = LinearLayoutManager(this@MypageHistoryActivity)
				val adapter = PhotoRecyclerAdapter(lPhoto)
				history_container.layoutManager = lm
				history_container.adapter = adapter
			}
		}
	}

	inner class PhotoRecyclerAdapter(val lPhoto: JsonArray) :
		RecyclerView.Adapter<PhotoRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val date = itemView.findViewById<TextView>(R.id.notice_date)
			val title = itemView.findViewById<TextView>(R.id.notice_title)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(this@MypageHistoryActivity).inflate(R.layout.mypage_history_photo_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return (lPhoto.size() + 1) / 2
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			// TODO("Match data to layout")
			// TODO("Set onClickListener --> to notice_detail")
		}
	}

	inner class HistoryRecyclerAdapter(val lHistory: JsonArray) :
		RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val alarm_title = itemView.findViewById<TextView>(R.id.alarm_title)
			val start_date = itemView.findViewById<TextView>(R.id.start_date)
			val end_date = itemView.findViewById<TextView>(R.id.end_date)
			val alarm_count = itemView.findViewById<TextView>(R.id.alarm_count)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(this@MypageHistoryActivity).inflate(R.layout.mypage_history_all_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lHistory.size()
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			// TODO("Set click popup")
		}
	}
}
