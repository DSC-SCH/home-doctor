package com.khnsoft.zipssa

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.list_fragment.*
import kotlinx.android.synthetic.main.sync_drawer.*
import java.text.SimpleDateFormat
import java.util.*

class MainListFragment : Fragment() {

	companion object {
		const val RC_DATE = 200;
		const val EXTRA_DATE = "date";

		lateinit var frag: MainListFragment

		fun getInstance(): MainListFragment {
			if (!::frag.isInitialized) {
				frag = MainListFragment().apply { arguments = Bundle() }
				return frag
			} else return frag
		}
	}

	val sdf_main_date = SimpleDateFormat("yyyy / MM / dd", Locale.KOREA)
	val sdf_date_save = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.list_fragment, container, false)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val curDateCal = Calendar.getInstance()
		main_date.text = sdf_main_date.format(curDateCal.time)

		main_date.setOnClickListener {
			val intent = Intent(context, MainListCalendarActivity::class.java)
			startActivityForResult(intent, RC_DATE)
		}

		main_date_before.setOnClickListener {
			curDateCal.add(Calendar.DAY_OF_MONTH, -1)
			main_date.text = sdf_main_date.format(curDateCal.time)
			refresh()
		}

		main_date_next.setOnClickListener {
			curDateCal.add(Calendar.DAY_OF_MONTH, 1)
			main_date.text = sdf_main_date.format(curDateCal.time)
			refresh()
		}

		// TODO("Move drawer to main_activity")

		drawer_open_btn.setOnClickListener {
			sync_drawer.openDrawer(Gravity.RIGHT)
		}

		close_btn.setOnClickListener {
			sync_drawer.closeDrawer(Gravity.RIGHT)
		}

		edit_btn.setOnClickListener {
			val intent = Intent(context, MypageSyncActivity::class.java)
			startActivity(intent)
		}
	}

	override fun onResume() {
		super.onResume()
		refresh()
	}

	fun refresh() {
		val curCal = Calendar.getInstance()
		val curDate = main_date.text.toString()
		curCal.time = sdf_main_date.parse(curDate)

		val lTemp = ServerHandler.send(context, EndOfAPI.GET_ENABLED_ALARMS)["array"].asJsonArray

		val lAllAlarms = JsonArray()

		for (item in lTemp) {
			val jItem = ServerHandler.convertKeys(item.asJsonObject, ServerHandler.alarmToLocal)

			if (sdf_date_save.parse(jItem["alarm_start_date"].asString).time <= curCal.timeInMillis &&
				curCal.timeInMillis <= sdf_date_save.parse(jItem["alarm_end_date"].asString).time &&
				!jItem["alarm_times"].asString.isBlank()
			)
				lAllAlarms.add(jItem)
		}

		val lAlarms = JsonArray()
		lAlarms.add(JsonObject())
		for (item in lAllAlarms) {
			val jItem = item.asJsonObject
			if (jItem["alarm_repeats"].asString.contains("${curCal[Calendar.DAY_OF_WEEK]}"))
				lAlarms.add(jItem)
		}

		val adapter = MainItemRecyclerAdapter(context, lAlarms, lAllAlarms)
		val lm = LinearLayoutManager(context)
		main_list_container.layoutManager = lm
		main_list_container.adapter = adapter
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == RC_DATE) {
			val retDate = data?.getStringExtra(EXTRA_DATE) ?: return
			main_date.text = retDate
		}
	}

	class MainItemRecyclerAdapter(val context: Context?, val lItems: JsonArray, val lAllItems: JsonArray) :
		RecyclerView.Adapter<RecyclerView.ViewHolder>() {
		val TYPE_REMAIN = 1
		val TYPE_ITEM = 2

		val curCal = Calendar.getInstance()
		val sdf_date_save = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
		val sdf_date_show = SimpleDateFormat("yy.MM.dd", Locale.KOREA)
		val sdf_dt_next = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)
		val DAY_IN_MILLIS = 24 * 60 * 60 * 1000

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
				val endDate = sdf_date_save.parse(jItem["alarm_end_date"].asString)
				val day_dday =
					endDate.time / DAY_IN_MILLIS - sdf_date_save.parse(sdf_date_save.format(curCal.time)).time / DAY_IN_MILLIS
				val lm = LinearLayoutManager(context)
				val adapter = MainItemTimeRecyclerAdapter(context, lTimes)

				if (lTimes.size() > 0 && lRepeats.size() > 0) {
					val nextCal = Calendar.getInstance()
					var temp = ""
					selectDay@ while (true) {
						for (weekday in lRepeats) {
							if (weekday.asInt == nextCal[Calendar.DAY_OF_WEEK]) {
								for (i in 0..lTimes.size() - 1) {
									temp = lTimes[i].asString
									if (curCal.timeInMillis < sdf_dt_next.parse("${sdf_date_save.format(nextCal.time)} ${temp}").time) {
										break@selectDay
									}
								}
							}
						}
						nextCal.add(Calendar.DAY_OF_MONTH, 1)
					}

					// TODO("Null if next time is after the end")
					val timeLeft =
						sdf_dt_next.parse("${sdf_date_save.format(nextCal.time)} ${temp}").time - curCal.timeInMillis
					time_remain.text =
						"${if (timeLeft > DAY_IN_MILLIS)
							"${timeLeft / DAY_IN_MILLIS}d ${timeLeft % DAY_IN_MILLIS / (60 * 60 * 1000)}h ${timeLeft / (60 * 1000) % 60}m"
						else
							"${timeLeft % DAY_IN_MILLIS / (60 * 60 * 1000)}h ${timeLeft / (60 * 1000) % 60}m"}"
				} else {
					time_remain.text = "필요 시"
				}

				title.text = jItem["alarm_title"].asString
				title.setBackgroundColor(Color.parseColor("${jItem["label_color"].asString}"))

				count.text = "${lTimes.size()}회 복용"
				time_container.layoutManager = lm
				time_container.adapter = adapter
				switch.isChecked = AlarmParser.parseStatus(jItem["alarm_enabled"].asString) == AlarmStatus.ENABLED
				start_date.text = sdf_date_show.format(sdf_date_save.parse(jItem["alarm_start_date"].asString))
				end_date.text = sdf_date_show.format(endDate)
				dday.text = "D-${day_dday}"

				itemView.setOnClickListener {
					val intent = Intent(context, MainItemPopup::class.java)
					intent.putExtra("jItem", jItem.toString())
					context?.startActivity(intent)
				}

				// TODO("Checkbox or '0/0' after alarm function and medilog tb")
			}
		}

		inner class RemainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val title = itemView.findViewById<TextView>(R.id.main_remain_title)
			val time_remain = itemView.findViewById<TextView>(R.id.main_remain_time)

			fun setRemainDetails() {
				var jItem: JsonObject
				var lTimes: JsonArray
				var lRepeats: JsonArray
				var nextIndex = -1
				var nextTime = Long.MAX_VALUE

				for (i in 0..lAllItems.size() - 1) {
					jItem = lAllItems[i].asJsonObject
					lTimes = AlarmParser.parseTimes(jItem["alarm_times"].asString)
					lRepeats = AlarmParser.parseRepeats(jItem["alarm_repeats"].asString)

					Log.i("@@@", jItem.toString())
					if (lTimes.size() > 0 && lRepeats.size() > 0) {
						val nextCal = Calendar.getInstance()
						var temp = ""
						selectDay@ while (true) {
							for (weekday in lRepeats) {
								if (weekday.asInt == nextCal[Calendar.DAY_OF_WEEK]) {
									for (j in 0..lTimes.size() - 1) {
										temp = lTimes[j].asString
										if (curCal.timeInMillis < sdf_dt_next.parse("${sdf_date_save.format(nextCal.time)} ${temp}").time) {
											break@selectDay
										}
									}
								}
							}
							nextCal.add(Calendar.DAY_OF_MONTH, 1)
						}

						val timeLeft =
							sdf_dt_next.parse("${sdf_date_save.format(nextCal.time)} ${temp}").time - curCal.timeInMillis
						if (timeLeft < nextTime) {
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
				}

				// TODO("Display text when there is no next alarm")
			}
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
			val view: View
			if (viewType == TYPE_REMAIN) {
				view = LayoutInflater.from(context).inflate(R.layout.list_remain, parent, false)
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

	class MainItemTimeRecyclerAdapter(val context: Context?, val lTimes: JsonArray) :
		RecyclerView.Adapter<MainItemTimeRecyclerAdapter.ViewHolder>() {
		val sdf_time_save = SimpleDateFormat("HH:mm", Locale.KOREA)
		val sdf_time_show = SimpleDateFormat("a h:mm", Locale.ENGLISH)

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
			holder.time.text = sdf_time_show.format(sdf_time_save.parse(lTimes[position].asString)).toLowerCase()
		}
	}
}