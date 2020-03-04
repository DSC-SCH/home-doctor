package com.khnsoft.zipssa

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.mypage_notice_activity.*

class MypageNoticeActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_notice_activity)

		back_btn.setOnClickListener { onBackPressed() }

		// TODO("Get notice from server")
		val adapter = NoticeRecyclerAdapter(this@MypageNoticeActivity, JsonArray())
		val lm = LinearLayoutManager(this@MypageNoticeActivity)
		notice_container.layoutManager = lm
		notice_container.adapter = adapter
	}

	class NoticeRecyclerAdapter(val context: Context?, val lNotice: JsonArray) :
			RecyclerView.Adapter<NoticeRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val date = itemView.findViewById<TextView>(R.id.notice_date)
			val title = itemView.findViewById<TextView>(R.id.notice_title)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(context).inflate(R.layout.mypage_notice_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lNotice.size()
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			// TODO("Match data to layout")
			// TODO("Set onClickListener --> to notice_detail")
		}
	}
}
