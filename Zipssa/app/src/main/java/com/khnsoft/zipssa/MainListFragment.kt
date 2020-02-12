package com.khnsoft.zipssa

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.main_list_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class MainListFragment : Fragment() {
	val sdf_time_show = SimpleDateFormat("a hh:mm", Locale.KOREA)
	val sdf_main_date = SimpleDateFormat("yyyy / MM / dd", Locale.KOREA)
	val sdf_time_save = SimpleDateFormat("HH:mm", Locale.KOREA)
	val sdf_date_save = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.main_list_fragment, container, false)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val context = view.context

		val curDateCal = Calendar.getInstance()
		main_date.text = sdf_main_date.format(curDateCal.time)

		main_date.setOnClickListener {
			DatePickerDialog(context, { _, year, month, dayOfMonth ->
				curDateCal[Calendar.YEAR] = year
				curDateCal[Calendar.MONTH] = month
				curDateCal[Calendar.DAY_OF_MONTH] = dayOfMonth
				main_date.text = sdf_main_date.format(curDateCal.time)
				refresh()
			}, curDateCal[Calendar.YEAR], curDateCal[Calendar.MONTH], curDateCal[Calendar.DAY_OF_MONTH]).show()
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
	}

	override fun onResume() {
		super.onResume()
		refresh()
	}

	companion object {
		lateinit var frag: MainListFragment

		fun getInstance(): MainListFragment {
			if (!::frag.isInitialized) {
				frag = MainListFragment().apply { arguments = Bundle() }
				return frag
			} else return frag
		}
	}

	fun refresh() {
		main_list_container.removeAllViews()
		val curCal = Calendar.getInstance()
		curCal.time = sdf_main_date.parse(main_date.text.toString())
		val curDate = sdf_date_save.format(curCal.time)

		val sql = """
			|SELECT _ID, TITLE, START_DT, END_DT, TIMES, REPEATS, ALARM, LABEL FROM ALARMS WHERE START_DT <= "${curDate}" AND END_DT >= "${curDate}" AND REPEATS LIKE "%${curCal[Calendar.DAY_OF_WEEK]}%"
		""".trimMargin()

		Log.i("${context?.packageName} - MainList", sql)
		val myHandler = DBHandler.open(context)
		val lAlarms = myHandler.execResult(sql)
		Log.i("@@@", lAlarms.toString())

		val adapter = MainItemRecyclerAdapter(context, lAlarms)
		val lm = LinearLayoutManager(context)
		main_list_container.layoutManager = lm
		main_list_container.adapter = adapter

		myHandler.close()
	}
}

class MainItemRecyclerAdapter(context: Context?, lItems: JsonArray) :
	RecyclerView.Adapter<MainItemRecyclerAdapter.ViewHolder>() {
	val context: Context?
	val lItems: JsonArray
	val curCal: Calendar
	val sdf_date_save = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
	val sdf_date_show = SimpleDateFormat("yy.MM.dd", Locale.KOREA)
	val sdf_time_save = SimpleDateFormat("HH:mm", Locale.KOREA)
	val sdf_dt_next = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)
	val DAY_IN_MILLIS = 24 * 60 * 60 * 1000

	init {
		this.context = context
		this.lItems = lItems

		curCal = Calendar.getInstance()
	}

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val time_remain = itemView.findViewById<TextView>(R.id.main_item_remain_time)
		val title = itemView.findViewById<TextView>(R.id.main_item_title)
		val count = itemView.findViewById<TextView>(R.id.main_item_count)
		val switch = itemView.findViewById<ToggleButton>(R.id.main_item_switch)
		val time_container = itemView.findViewById<RecyclerView>(R.id.main_item_time_container)
		val check_container = itemView.findViewById<LinearLayout>(R.id.main_item_check_container)
		val start_date = itemView.findViewById<TextView>(R.id.main_item_start_date)
		val end_date = itemView.findViewById<TextView>(R.id.main_item_end_date)
		val dday = itemView.findViewById<TextView>(R.id.main_item_dday)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(context).inflate(R.layout.main_item, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return lItems.size()
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val jItem = lItems[position].asJsonObject
		val lTimes = JsonParser.parseString(jItem["TIMES"]?.asString).asJsonArray
		val lRepeats = JsonParser.parseString(jItem["REPEATS"].asString).asJsonArray
		val endDate = sdf_date_save.parse(jItem["END_DT"].asString)
		val dday = endDate.time / DAY_IN_MILLIS - sdf_date_save.parse(sdf_date_save.format(curCal.time)).time / DAY_IN_MILLIS
		val lm = LinearLayoutManager(context)
		val adapter = MainItemTimeRecyclerAdapter(context, lTimes)
		val curTime = sdf_time_save.format(curCal.time)

		if (lTimes.size() > 0 && lRepeats.size() > 0) {
			val nextCal = Calendar.getInstance()
			var temp = ""
			selectDay@ while (true) {
				for (i in lRepeats) {
					if (i.asInt == nextCal[Calendar.DAY_OF_WEEK]) {
						for (i in 0..lTimes.size()-1) {
							temp = lTimes[i].asString
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
			holder.time_remain.text =
				"${if (timeLeft > DAY_IN_MILLIS)
					"${timeLeft / DAY_IN_MILLIS}d ${timeLeft % DAY_IN_MILLIS / (60 * 60 * 1000)}h ${timeLeft / (60 * 1000) % 60}m"
				else
					"${timeLeft % DAY_IN_MILLIS / (60 * 60 * 1000)}h ${timeLeft / (60 * 1000) % 60}m"}"
		} else {
			holder.time_remain.text = "필요 시"
		}

		holder.title.text = jItem["TITLE"].asString
		// TODO("Change title background color depend to label color")

		holder.count.text = "${lTimes.size()}회 복용"
		holder.time_container.layoutManager = lm
		holder.time_container.adapter = adapter
		holder.switch.isChecked = if (jItem["ALARM"].asInt == 0) false else true
		holder.start_date.text = sdf_date_show.format(sdf_date_save.parse(jItem["START_DT"].asString))
		holder.end_date.text = sdf_date_show.format(endDate)
		holder.dday.text = "D-${dday}"

		// TODO("Checkbox or '0/0'")
	}
}

class MainItemTimeRecyclerAdapter(context: Context?, lTimes: JsonArray) :
	RecyclerView.Adapter<MainItemTimeRecyclerAdapter.ViewHolder>() {
	val sdf_time_save = SimpleDateFormat("HH:mm", Locale.KOREA)
	val sdf_time_show = SimpleDateFormat("a h:mm", Locale.ENGLISH)
	val context: Context?
	val lTimes: JsonArray

	init {
		this.context = context
		this.lTimes = lTimes
	}

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val time = itemView.findViewById<TextView>(R.id.main_item_time)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(context).inflate(R.layout.main_item_time_item, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return lTimes.size()
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.time.text = sdf_time_show.format(sdf_time_save.parse(lTimes[position].asString)).toLowerCase()
	}
}