package com.khnsoft.zipssa

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.mypage_history_activity.*
import java.text.SimpleDateFormat
import java.util.*

class MypageHistoryActivity : AppCompatActivity() {
	companion object {
		const val PAGE_ALL = 0
		const val PAGE_PHOTO = 1
	}

	val sdf_date_show = SimpleDateFormat("yy.MM.dd", Locale.KOREA)
	val sdf_date_created = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
	val sdf_date_save = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_history_activity)

		back_btn.setOnClickListener { onBackPressed() }

		val radioGroup = MyRadioGroup()
		radioGroup.add(history_all)
		radioGroup.add(history_photo)

		radioGroup.setOnChangeListener(object: MyRadioGroup.OnChangeListener {
			override fun onChange(after: RadioButton) {

			}
		})

		changePage(PAGE_ALL)
	}

	fun changePage(page: Int) {
		when (page) {
			PAGE_ALL -> {
				val lHistory = ServerHandler.send(this@MypageHistoryActivity, EndOfAPI.GET_ALL_ALARMS)["data"].asJsonArray

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
			holder.start_date.text = sdf_date_show.format(sdf_date_save.parse(jItem["alarm_start_date"].asString))
			holder.end_date.text = sdf_date_show.format(sdf_date_save.parse(jItem["alarm_end_date"].asString))
			holder.alarm_count.text = AlarmParser.parseTimes(jItem["alarm_times"].asString).size().toString()
			holder.created_date.text = sdf_date_created.format(sdf_date_save.parse(jItem["created_date"].asString))
			holder.container.setOnClickListener {
				val intent = Intent(this@MypageHistoryActivity, MainItemPopup::class.java)
				intent.putExtra("jItem", jItem.toString())
				startActivity(intent)
			}
		}
	}
}
