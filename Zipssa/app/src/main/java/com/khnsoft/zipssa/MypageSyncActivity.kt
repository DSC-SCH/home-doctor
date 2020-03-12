package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.mypage_sync_activity.*

class MypageSyncActivity : AppCompatActivity() {
	companion object {
		const val PAGE_MANAGER = 0
		const val PAGE_CREW = 1
	}

	var curPage: Int = PAGE_MANAGER
	var isEdit = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_sync_activity)

		back_btn.setOnClickListener { onBackPressed() }

		val radioGroup = MyRadioGroup()
		radioGroup.add(manage_manager)
		radioGroup.add(manage_crew)

		edit_btn.setOnClickListener {
			isEdit = !isEdit
			refresh()
		}

		manage_manager.setOnClickListener {
			changePage(PAGE_MANAGER)
		}

		manage_crew.setOnClickListener {
			changePage(PAGE_CREW)
		}

		// TODO("Validation code after deadline")
	}

	fun changePage(page: Int) {
		curPage = page
		refresh()
	}

	fun refresh() {
		val lSync: JsonArray = when (curPage) {
			PAGE_MANAGER -> {
				ServerHandler.send(this@MypageSyncActivity, EndOfAPI.SYNC_GET_MANAGERS)["data"].asJsonArray
			}

			PAGE_CREW -> {
				ServerHandler.send(this@MypageSyncActivity, EndOfAPI.SYNC_GET_CREWS)["data"].asJsonArray
			}

			else -> return
		}

		val lm = LinearLayoutManager(this@MypageSyncActivity)
		val adapter = SyncRecyclerAdapter(lSync)
		sync_container.layoutManager = lm
		sync_container.adapter = adapter

		if (isEdit) {
			edit_btn.text = "완료"
			for (i in 0..sync_container.size-1) {
				val holder = sync_container.findViewHolderForAdapterPosition(i) as SyncRecyclerAdapter.ViewHolder
				holder.remove.visibility = View.VISIBLE
			}

			add_btn.visibility = View.VISIBLE
			when(curPage) {
				PAGE_MANAGER -> {
					add_btn.text = "매니저 추가"
					add_btn.setOnClickListener {
						val intent = Intent(this@MypageSyncActivity, MypageSyncManagerPopup::class.java)
						startActivity(intent)
					}
				}

				PAGE_CREW -> {
					add_btn.text = "크루 추가"
					add_btn.setOnClickListener {
						val intent = Intent(this@MypageSyncActivity, MypageSyncCrewPopup::class.java)
						startActivity(intent)
					}
				}
			}
		} else {
			edit_btn.text = "수정"
			add_btn.visibility = View.GONE
		}
	}

	override fun onResume() {
		super.onResume()
		refresh()
	}

	inner class SyncRecyclerAdapter(val lSync: JsonArray) :
		RecyclerView.Adapter<SyncRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val container = itemView.findViewById<LinearLayout>(R.id.sync_item)
			val name = itemView.findViewById<TextView>(R.id.sync_name)
			val remove = itemView.findViewById<ImageView>(R.id.sync_remove)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(this@MypageSyncActivity).inflate(R.layout.mypage_sync_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lSync.size()
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			val jItem = lSync[position].asJsonObject
			holder.name.text = jItem["name"].asString
			holder.remove.setOnClickListener {
				val data = MyAlertPopup.Data(AlertType.ALERT).apply {
					alertTitle = jItem["name"].asString
					alertContent = "해당 사용자와 연동을 끊으시겠습니까?"
					alertConfirmText = "삭제"
					confirmListener = View.OnClickListener {
						val result = ServerHandler.send(this@MypageSyncActivity, EndOfAPI.SYNC_DELETE, id=jItem["connectionId"].asInt)
						if (!HttpHelper.isOK(result)) {
							return@OnClickListener
						}

						val data = MyAlertPopup.Data(AlertType.CONFIRM)
						data.alertTitle = alertTitle
						data.alertContent = "해당 연동이 끊어졌습니다."
						val dataId = DataPasser.insert(data)

						val intent = Intent(this@MypageSyncActivity, MyAlertPopup::class.java)
						intent.putExtra(MyAlertPopup.EXTRA_DATA, dataId)
						startActivity(intent)
					}
				}

				val dataId = DataPasser.insert(data)
				val intent = Intent(this@MypageSyncActivity, MyAlertPopup::class.java)
				intent.putExtra(MyAlertPopup.EXTRA_DATA, dataId)
				startActivity(intent)
			}
		}
	}
}
