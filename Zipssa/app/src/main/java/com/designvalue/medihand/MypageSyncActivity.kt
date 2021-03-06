package com.designvalue.medihand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
			checkIsEdit()
		}

		radioGroup.setOnChangeListener(object: MyRadioGroup.OnChangeListener {
			override fun onChange(after: RadioButton) {
				changePage(if (after.id == R.id.manage_manager) PAGE_MANAGER else PAGE_CREW)
			}
		})
	}

	fun changePage(page: Int) {
		curPage = page
		refresh()
		checkIsEdit()
	}

	fun refresh() {
		val result = when (curPage) {
			PAGE_MANAGER -> {
				ServerHandler.send(this@MypageSyncActivity, EndOfAPI.SYNC_GET_MANAGERS)
			}

			PAGE_CREW -> {
				ServerHandler.send(this@MypageSyncActivity, EndOfAPI.SYNC_GET_CREWS)
			}

			else -> return
		}

		if (!HttpHelper.isOK(result)) {
			Toast.makeText(this@MypageSyncActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
			return
		}

		val lSync = result["data"].asJsonArray

		val lm = LinearLayoutManager(this@MypageSyncActivity)
		val adapter = SyncRecyclerAdapter(lSync)
		sync_container.layoutManager = lm
		sync_container.adapter = adapter
	}

	fun checkIsEdit() {
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
			for (i in 0..sync_container.size-1) {
				val holder = sync_container.findViewHolderForAdapterPosition(i) as SyncRecyclerAdapter.ViewHolder
				holder.remove.visibility = View.GONE
			}
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
			holder.name.text = jItem["username"].asString
			holder.remove.setOnClickListener {
				val data = MyAlertPopup.Data(AlertType.ALERT).apply {
					alertTitle = jItem["username"].asString
					alertContent = "연동을 해지하시겠습니까?\n해지 시 상대방의 리스트에서도 연동이 해지됩니다."
					alertConfirmText = "삭제"
					confirmListener = View.OnClickListener {
						val result = ServerHandler.send(this@MypageSyncActivity, EndOfAPI.SYNC_DELETE, id=jItem["connectionId"].asInt)
						if (!HttpHelper.isOK(result)) {
							Toast.makeText(this@MypageSyncActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
							return@OnClickListener
						}

						val data = MyAlertPopup.Data(AlertType.CONFIRM)
						data.alertTitle = alertTitle
						data.alertContent = "해당 연동이 끊어졌습니다."
						val dataId = DataPasser.insert(data)

						val intent = Intent(this@MypageSyncActivity, MyAlertPopup::class.java)
						intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
						startActivity(intent)
					}
				}

				val dataId = DataPasser.insert(data)
				val intent = Intent(this@MypageSyncActivity, MyAlertPopup::class.java)
				intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
				startActivity(intent)
			}

			holder.remove.visibility = if (isEdit) View.VISIBLE else View.GONE
		}
	}
}
