package com.khnsoft.zipssa

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.search_result_fragment.*

class SearchResultFragment : Fragment() {
	var lResult: JsonArray? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.search_result_fragment, container, false)
		return view
	}

	companion object {
		var frag : SearchResultFragment? = null

		fun getInstance(): SearchResultFragment {
			if (frag == null) frag = SearchResultFragment().apply { arguments = Bundle() }
			return frag!!
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		refresh()
	}

	// TODO("Remove page system: Due to not enough data")

	fun refresh() {
		if (lResult == null) return
		val adapter = SearchResultRecyclerAdapter(lResult!!)
		val lm = LinearLayoutManager(context)
		search_result_container.layoutManager = lm
		search_result_container.adapter = adapter
	}

	inner class SearchResultRecyclerAdapter(val lResult: JsonArray) :
		RecyclerView.Adapter<SearchResultRecyclerAdapter.ViewHolder>(){

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val container = itemView.findViewById<LinearLayout>(R.id.medicine_container)
			val name_ko = itemView.findViewById<TextView>(R.id.medicine_name_ko)
			val name_ = itemView.findViewById<TextView>(R.id.medicine_name_en)
			val function = itemView.findViewById<TextView>(R.id.medicine_function)
			val effect = itemView.findViewById<TextView>(R.id.medicine_effect)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultRecyclerAdapter.ViewHolder {
			val view = LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lResult.size()
		}

		override fun onBindViewHolder(holder: SearchResultRecyclerAdapter.ViewHolder, position: Int) {
			val jItem = lResult[position].asJsonObject

			holder.container.setOnClickListener {
				val intent = Intent(context, MedicineDetailActivity::class.java)
				intent.putExtra(ExtraAttr.MEDICINE, jItem.toString())
				startActivity(intent)
			}
			// TODO("Server: Assign data to TextView")
		}
	}
}