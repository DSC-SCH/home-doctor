package com.khnsoft.zipssa

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.search_fragment.*

class MainSearchFragment : Fragment() {
	val FRAG_RECENT = 0
	val FRAG_RESULT = 1

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.search_fragment, container, false)
		return view
	}

	companion object {
		lateinit var frag: MainSearchFragment

		fun getInstance(): MainSearchFragment {
			if (!::frag.isInitialized) {
				frag = MainSearchFragment().apply { arguments = Bundle() }
				return frag
			} else return frag
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val search_type_array = resources.getStringArray(R.array.search_type)
		val adapter = ArrayAdapter<String>(context!!, R.layout.search_type_spinner_item, search_type_array)
		adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
		search_type.adapter = adapter

		search_btn.setOnClickListener {
			search(search_type.selectedItem.toString(), search_text.text.toString())
		}

		val intent = Intent(context, MedicineDetailActivity::class.java)
		startActivity(intent)

		callPage(FRAG_RECENT, null)
	}

	fun search(type: String, keyword: String) {
		val lResult = JsonArray()

		// TODO("Connect to server for searching result")

		callPage(FRAG_RESULT, lResult)
	}

	fun callPage(no: Int, lResult: JsonArray?) {
		val transaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()

		when (no) {
			FRAG_RECENT -> {
				val frag = SearchRecentFragment.getInstance()
				frag.cur_context = this@MainSearchFragment
				transaction.replace(R.id.search_container, frag)
				transaction.commit()
			}

			FRAG_RESULT -> {
				val frag = SearchResultFragment.getInstance()
				frag.lResult = lResult!!
				transaction.replace(R.id.search_container, frag)
				transaction.commit()
			}
		}
	}
}