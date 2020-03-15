package com.khnsoft.zipssa

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.list_fragment.*
import kotlinx.android.synthetic.main.sync_drawer.*
import java.util.*

class MainListFragment : Fragment() {
	var viewingDate : String? = null

	companion object {
		const val RC_DATE = 200;
		const val DAY_IN_MILLIS = 24 * 60 * 60 * 1000
		const val STATE_DATE = "date"

		var frag : MainListFragment? = null

		fun getInstance(): MainListFragment {
			if (frag == null) frag = MainListFragment().apply { arguments = Bundle() }
			return frag!!
		}
	}

	// TODO("Reject approach to online only function")

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.list_fragment, container, false)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val curDateCal = Calendar.getInstance()
		val dateTemp = viewingDate ?: SDF.dateSlash.format(curDateCal.time)
		main_date.text = dateTemp
		curDateCal.time = SDF.dateSlash.parse(dateTemp)

		main_date.setOnClickListener {
			val intent = Intent(context, MainListCalendarActivity::class.java)
			intent.putExtra(ExtraAttr.CALENDAR_START, main_date.text.toString())
			startActivityForResult(intent, RC_DATE)
		}

		main_date_before.setOnClickListener {
			curDateCal.add(Calendar.DAY_OF_MONTH, -1)
			main_date.text = SDF.dateSlash.format(curDateCal.time)
			refresh()
		}

		main_date_next.setOnClickListener {
			curDateCal.add(Calendar.DAY_OF_MONTH, 1)
			main_date.text = SDF.dateSlash.format(curDateCal.time)
			refresh()
		}

		sync_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

		drawer_open_btn.setOnClickListener {
			if (!UserData.isOnline()) {
				MyAlertPopup.needOnline(context)
			}

			sync_drawer.openDrawer(Gravity.RIGHT)
		}

		sync_drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
			override fun onDrawerStateChanged(newState: Int) {
			}

			override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
			}

			override fun onDrawerClosed(drawerView: View) {
				crew_container.removeAllViews()
				sync_back.visibility = if (UserData.careUser == null) View.GONE else View.VISIBLE
			}

			override fun onDrawerOpened(drawerView: View) {
				sync_name.text = UserData.name
				val lSync = ServerHandler.send(context, EndOfAPI.SYNC_GET_CREWS)["data"].asJsonArray
				val lm = LinearLayoutManager(context)
				val adapter = SyncRecyclerAdapter(lSync)
				crew_container.layoutManager = lm
				crew_container.adapter = adapter
			}
		})

		sync_back.setOnClickListener {
			UserData.careUser = null
			UserData.careName = null
			sync_drawer.closeDrawer(Gravity.RIGHT)
		}

		close_btn.setOnClickListener {
			sync_drawer.closeDrawer(Gravity.RIGHT)
		}

		edit_btn.setOnClickListener {
			val intent = Intent(context, MypageSyncActivity::class.java)
			startActivity(intent)
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		viewingDate = main_date.text.toString()
	}

	override fun onResume() {
		super.onResume()
		refresh()
	}

	fun refresh() {
		main_name.text = UserData.name
		main_name_bar.setBackgroundColor(if (UserData.careUser == null) 0 else resources.getColor(R.color.main_theme))

		val curCal = Calendar.getInstance()
		val curDate = main_date.text.toString()
		curCal.time = SDF.dateSlash.parse(curDate)

		val lTemp: JsonArray =
			if (UserData.careUser == null) ServerHandler.send(context, EndOfAPI.GET_ENABLED_ALARMS)["data"].asJsonArray
			else ServerHandler.send(context, EndOfAPI.SYNC_GET_ALL_ALARMS, id = UserData.careUser)["data"].asJsonArray

		val lAllAlarms = JsonArray()

		for (item in lTemp) {
			val jItem = ServerHandler.convertKeys(item.asJsonObject, ServerHandler.alarmToLocal)

			if (!jItem["alarm_times"].asString.isBlank())
				lAllAlarms.add(jItem)
		}

		val lAlarms = JsonArray()
		lAlarms.add(JsonObject())
		for (item in lAllAlarms) {
			val jItem = item.asJsonObject
			if (SDF.dateBar.parse(jItem["alarm_start_date"].asString).time <= curCal.timeInMillis &&
				curCal.timeInMillis <= SDF.dateBar.parse(jItem["alarm_end_date"].asString).time &&
				jItem["alarm_repeats"].asString.contains(curCal[Calendar.DAY_OF_WEEK].toString())
			)
				lAlarms.add(jItem)
		}

		val adapter = MainItemRecyclerAdapter(lAlarms, lAllAlarms)
		val lm = LinearLayoutManager(context)
		main_list_container.layoutManager = lm
		main_list_container.adapter = adapter
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == RC_DATE) {
			val retDate = data?.getStringExtra(ExtraAttr.CALENDAR_DATE) ?: return
			main_date.text = retDate
		}
	}

	inner class SyncRecyclerAdapter(val lSync: JsonArray) :
		RecyclerView.Adapter<SyncRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val container = itemView.findViewById<LinearLayout>(R.id.sync_item)
			val name = itemView.findViewById<TextView>(R.id.sync_name)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(context).inflate(R.layout.mypage_sync_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lSync.size()
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			val jItem = lSync[position].asJsonObject
			holder.name.text = jItem["username"].asString
			if (UserData.careUser == jItem["user"].asInt) holder.name.setBackgroundColor(resources.getColor(R.color.button_theme))
			holder.container.setOnClickListener {
				UserData.careUser = jItem["user"].asInt
				UserData.careName = jItem["username"].asString
				sync_drawer.closeDrawer(Gravity.RIGHT)
				refresh()
			}
		}
	}

	inner class MainItemRecyclerAdapter(val lItems: JsonArray, val lAllItems: JsonArray) :
		RecyclerView.Adapter<RecyclerView.ViewHolder>() {
		val TYPE_REMAIN = 1
		val TYPE_ITEM = 2

		val curCal = Calendar.getInstance()

		inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val time_remain = itemView.findViewById<TextView>(R.id.main_item_remain_time)
			val title = itemView.findViewById<TextView>(R.id.main_item_title)
			val count = itemView.findViewById<TextView>(R.id.main_item_count)
			val switch = itemView.findViewById<Switch>(R.id.main_item_switch)
			val time_container = itemView.findViewById<RecyclerView>(R.id.main_item_time_container)
			val check_container = itemView.findViewById<LinearLayout>(R.id.main_item_check_container)
			val start_date = itemView.findViewById<TextView>(R.id.main_item_start_date)
			val end_date = itemView.findViewById<TextView>(R.id.main_item_end_date)
			val dday = itemView.findViewById<TextView>(R.id.main_item_dday)

			fun setItemDetails(jItem: JsonObject) {
				val lTimes = AlarmParser.parseTimes(jItem["alarm_times"].asString)
				val lRepeats = AlarmParser.parseRepeats(jItem["alarm_repeats"].asString)
				val startDate = SDF.dateBar.parse(jItem["alarm_start_date"].asString)
				val endDate = SDF.dateBar.parse(jItem["alarm_end_date"].asString)
				val day_dday =
					endDate.time / DAY_IN_MILLIS - SDF.dateBar.parse(SDF.dateBar.format(curCal.time)).time / DAY_IN_MILLIS
				val lm = LinearLayoutManager(context)
				val adapter = MainItemTimeRecyclerAdapter(lTimes)

				if (lTimes.size() > 0 && lRepeats.size() > 0) {
					val nextCal = Calendar.getInstance()
					var temp = ""
					selectDay@ while (true) {
						for (weekday in lRepeats) {
							if (weekday.asInt == nextCal[Calendar.DAY_OF_WEEK]) {
								for (i in 0..lTimes.size() - 1) {
									temp = lTimes[i].asString
									val timeNext =
										SDF.dateTime.parse("${SDF.dateBar.format(nextCal.time)} ${temp}").time
									if (curCal.timeInMillis < timeNext && startDate.time < timeNext) {
										break@selectDay
									}
								}
							}
						}
						nextCal.add(Calendar.DAY_OF_MONTH, 1)
					}

					val dateNext = SDF.dateTime.parse("${SDF.dateBar.format(nextCal.time)} ${temp}")
					if (dateNext.time < endDate.time + DAY_IN_MILLIS) {
						val timeLeft = dateNext.time - curCal.timeInMillis
						time_remain.text =
							"${if (timeLeft > DAY_IN_MILLIS)
								"${timeLeft / DAY_IN_MILLIS}d ${timeLeft % DAY_IN_MILLIS / (60 * 60 * 1000)}h ${timeLeft / (60 * 1000) % 60}m"
							else
								"${timeLeft % DAY_IN_MILLIS / (60 * 60 * 1000)}h ${timeLeft / (60 * 1000) % 60}m"}"
					} else {
						time_remain.text = "완료됨"
					}
				} else {
					time_remain.text = "필요 시"
				}

				title.text = jItem["alarm_title"].asString
				title.setBackgroundColor(Color.parseColor(jItem["label_color"].asString))

				count.text = "${lTimes.size()}회 복용"
				time_container.layoutManager = lm
				time_container.adapter = adapter
				switch.isChecked = AlarmParser.parseStatus(jItem["alarm_enabled"].asString) == AlarmStatus.ENABLED
				start_date.text = SDF.dateDotShort.format(SDF.dateBar.parse(jItem["alarm_start_date"].asString))
				end_date.text = SDF.dateDotShort.format(endDate)
				dday.text = "D-${day_dday}"

				itemView.setOnClickListener {
					val intent = Intent(context, MainItemPopup::class.java)
					intent.putExtra(ExtraAttr.ALARM_ID, jItem["alarm_id"].asInt)
					context?.startActivity(intent)
				}

				if (UserData.careUser != null)
					switch.isEnabled = false
				else {
					switch.setOnCheckedChangeListener { buttonView, isChecked ->
						val json = JsonObject()
						json.addProperty(
							"alarm_enabled",
							if (isChecked) AlarmStatus.ENABLED.status else AlarmStatus.DISABLED.status
						)

						val result =
							ServerHandler.send(context, EndOfAPI.CHANGE_ALARM_STATE, json, jItem["alarm_id"].asInt)

						if (!HttpHelper.isOK(result))
							switch.isChecked = false
					}
				}

				// TODO("Checkbox or '0/0' after alarm function and medilog tb")
			}
		}

		inner class RemainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val title = itemView.findViewById<TextView>(R.id.main_remain_title)
			val time_remain = itemView.findViewById<TextView>(R.id.main_remain_time)
			val container = itemView.findViewById<LinearLayout>(R.id.main_remain_container)
			val box = itemView.findViewById<LinearLayout>(R.id.main_remain_box)

			fun setRemainDetails() {
				var jItem: JsonObject
				var lTimes: JsonArray
				var lRepeats: JsonArray
				var startDate: Date?
				var endDate: Date?
				var nextIndex = -1
				var nextTime = Long.MAX_VALUE

				for (i in 0..lAllItems.size() - 1) {
					jItem = lAllItems[i].asJsonObject
					lTimes = AlarmParser.parseTimes(jItem["alarm_times"].asString)
					lRepeats = AlarmParser.parseRepeats(jItem["alarm_repeats"].asString)
					startDate = SDF.dateBar.parse(jItem["alarm_start_date"].asString)
					endDate = SDF.dateBar.parse(jItem["alarm_end_date"].asString)

					MyLogger.d("MainListFragment", jItem.toString())
					if (lTimes.size() > 0 && lRepeats.size() > 0) {
						val nextCal = Calendar.getInstance()
						var temp = ""
						selectDay@ while (true) {
							for (weekday in lRepeats) {
								if (weekday.asInt == nextCal[Calendar.DAY_OF_WEEK]) {
									for (j in 0..lTimes.size() - 1) {
										temp = lTimes[j].asString
										val timeNext =
											SDF.dateTime.parse("${SDF.dateBar.format(nextCal.time)} ${temp}").time
										if (curCal.timeInMillis < timeNext && startDate.time < timeNext) {
											break@selectDay
										}
									}
								}
							}
							nextCal.add(Calendar.DAY_OF_MONTH, 1)
						}

						val timeNext = SDF.dateTime.parse("${SDF.dateBar.format(nextCal.time)} ${temp}").time
						val timeLeft = timeNext - curCal.timeInMillis
						if (timeNext < endDate.time + DAY_IN_MILLIS && timeLeft < nextTime) {
							nextTime = timeLeft
							nextIndex = i
						}
					} else {
						time_remain.text = "필요 시"
					}
				}

				if (nextIndex != -1) {
					val jNextItem = lAllItems[nextIndex].asJsonObject
					title.text = jNextItem["alarm_title"].asString
					time_remain.text = "${if (nextTime > DAY_IN_MILLIS)
						"${nextTime / DAY_IN_MILLIS}일 ${nextTime % DAY_IN_MILLIS / (60 * 60 * 1000)}시간 ${nextTime / (60 * 1000) % 60}분"
					else
						"${nextTime % DAY_IN_MILLIS / (60 * 60 * 1000)}시간 ${nextTime / (60 * 1000) % 60}분"}"
					box.setOnClickListener {
						val intent = Intent(context, MainItemPopup::class.java)
						intent.putExtra(ExtraAttr.ALARM_ID, jNextItem["alarm_id"].asInt)
						context?.startActivity(intent)
					}
				} else {
					container.removeAllViews()
					val view = LayoutInflater.from(context).inflate(R.layout.list_remain_empty_item, container, false)
					container.addView(view)
					val emptyBox = view.findViewById<LinearLayout>(R.id.main_remain_empty_box)
					emptyBox.setOnClickListener {
						val intent = Intent(context, AddAlarmActivity::class.java)
						startActivity(intent)
					}
				}
			}
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
			val view: View
			if (viewType == TYPE_REMAIN) {
				view = LayoutInflater.from(context).inflate(R.layout.list_remain_item, parent, false)
				return RemainViewHolder(view)
			}
			view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
			return ItemViewHolder(view)
		}

		override fun getItemViewType(position: Int): Int {
			if (position == 0)
				return TYPE_REMAIN
			return TYPE_ITEM
		}

		override fun getItemCount(): Int {
			return lItems.size()
		}

		override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
			if (getItemViewType(position) == TYPE_REMAIN)
				(holder as RemainViewHolder).setRemainDetails()
			else
				(holder as ItemViewHolder).setItemDetails(lItems[position].asJsonObject)
		}
	}

	inner class MainItemTimeRecyclerAdapter(val lTimes: JsonArray) :
		RecyclerView.Adapter<MainItemTimeRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val time = itemView.findViewById<TextView>(R.id.main_item_time)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(context).inflate(R.layout.list_item_time_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lTimes.size()
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			holder.time.text = SDF.timeInEnglish.format(SDF.time.parse(lTimes[position].asString)).toLowerCase()
		}
	}
}