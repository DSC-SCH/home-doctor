package com.designvalue.medihand_en

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_recent_fragment.*

class SearchRecentFragment : Fragment() {
	val cur_context = MainSearchFragment.getInstance()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.search_recent_fragment, container, false)
		return view
	}

	companion object {
		var frag : SearchRecentFragment? = null

		fun getInstance(): SearchRecentFragment {
			if (frag == null) frag = SearchRecentFragment().apply { arguments = Bundle() }
			return frag!!
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val sp = SPHandler.getSp(context!!)
		val lRecent = JsonParser.parseString(sp.getString(MainSearchFragment.SP_SEARCH, "[]")).asJsonArray

		val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
		val adapter = RecentRecyclerAdapter(lRecent)
		search_recent_container.layoutManager = lm
		search_recent_container.adapter = adapter
	}

	inner class RecentRecyclerAdapter(val lRecent: JsonArray) :
		RecyclerView.Adapter<RecentRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val title = itemView.findViewById<TextView>(R.id.recent_title)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(context).inflate(R.layout.search_recent_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lRecent.size()
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			val history = lRecent[itemCount-1-position].asString
			holder.title.text = history
			holder.title.setOnClickListener {
				cur_context.search(cur_context.search_type.selectedItemPosition, history)
			}
		}
	}
}