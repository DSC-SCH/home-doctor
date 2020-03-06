package com.khnsoft.zipssa

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.mypage_history_photo_activity.*

class MypageHistoryPhotoActivity : AppCompatActivity() {
	val PAGE_MANAGER = 0
	val PAGE_CREW = 1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_history_photo_activity)

		back_btn.setOnClickListener {
			onBackPressed()
		}

		val lPhoto = JsonArray()

	}

	class PhotoRecyclerAdapter(val context: Context?, val lPhoto: JsonArray) :
		RecyclerView.Adapter<PhotoRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val date = itemView.findViewById<TextView>(R.id.notice_date)
			val title = itemView.findViewById<TextView>(R.id.notice_title)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(context).inflate(R.layout.mypage_history_photo_item, parent, false)
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
}
