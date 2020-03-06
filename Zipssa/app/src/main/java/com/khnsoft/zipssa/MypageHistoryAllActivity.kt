package com.khnsoft.zipssa

import android.content.Context
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
import kotlinx.android.synthetic.main.mypage_history_all_activity.*

class MypageHistoryAllActivity : AppCompatActivity() {
	val PAGE_MANAGER = 0
	val PAGE_CREW = 1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_history_all_activity)

		back_btn.setOnClickListener {
			onBackPressed()
		}

		val lHistory = JsonArray()
		// TODO("Get all history from DB")

		val adapter = HistoryRecyclerAdapter(this@MypageHistoryAllActivity, lHistory)
		val lm = LinearLayoutManager(this@MypageHistoryAllActivity)
		history_all_container.layoutManager = lm
		history_all_container.adapter = adapter
	}

	class HistoryRecyclerAdapter(val context: Context?, val lHistory: JsonArray) :
		RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val alarm_title = itemView.findViewById<TextView>(R.id.alarm_title)
			val start_date = itemView.findViewById<TextView>(R.id.start_date)
			val end_date = itemView.findViewById<TextView>(R.id.end_date)
			val alarm_count = itemView.findViewById<TextView>(R.id.alarm_count)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(context).inflate(R.layout.mypage_history_all_item, parent, false)
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
