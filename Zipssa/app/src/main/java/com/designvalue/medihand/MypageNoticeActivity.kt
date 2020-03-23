package com.designvalue.medihand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.mypage_notice_activity.*

class MypageNoticeActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_notice_activity)

		back_btn.setOnClickListener { onBackPressed() }

		val result = ServerHandler.send(this@MypageNoticeActivity, EndOfAPI.GET_NOTICES)

		if (!HttpHelper.isOK(result)) {
			Toast.makeText(this@MypageNoticeActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
			finish()
		}
		val lNotice = result["data"].asJsonArray

		val adapter = NoticeRecyclerAdapter(lNotice)
		val lm = LinearLayoutManager(this@MypageNoticeActivity)
		notice_container.layoutManager = lm
		notice_container.adapter = adapter
	}

	inner class NoticeRecyclerAdapter(val lNotice: JsonArray) :
			RecyclerView.Adapter<NoticeRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val date = itemView.findViewById<TextView>(R.id.notice_date)
			val title = itemView.findViewById<TextView>(R.id.notice_title)
			val container = itemView.findViewById<LinearLayout>(R.id.notice_item_container)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(this@MypageNoticeActivity).inflate(R.layout.mypage_notice_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lNotice.size()
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			val jItem = lNotice[position].asJsonObject
			holder.title.text = jItem["title"].asString
			holder.date.text = SDF.dateDot.format(SDF.dateBar.parse(jItem["createdDate"].asString))
			holder.container.setOnClickListener {
				val intent = Intent(this@MypageNoticeActivity, MypageNoticeDetailActivity::class.java)
				intent.putExtra(ExtraAttr.NOTICE, jItem.toString())
				startActivity(intent)
			}
		}
	}
}
