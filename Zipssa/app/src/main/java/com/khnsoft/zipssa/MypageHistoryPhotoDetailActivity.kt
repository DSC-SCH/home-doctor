package com.khnsoft.zipssa

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.mypage_history_activity.*

class MypageHistoryPhotoDetailActivity : AppCompatActivity() {
	var cur_photo = -1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_history_photo_detail_activity)

		val lPhoto = JsonArray()

	}

	class PhotoRecyclerAdapter(val context: Context?, val lPhoto: JsonArray) :
		RecyclerView.Adapter<PhotoRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val image = itemView.findViewById<ImageView>(R.id.image_item)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(context).inflate(R.layout.mypage_history_photo_detail_list_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lPhoto.size() / 2
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {

		}
	}
}
