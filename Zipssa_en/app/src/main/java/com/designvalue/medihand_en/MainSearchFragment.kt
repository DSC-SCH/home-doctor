package com.designvalue.medihand_en

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.search_fragment.*

class MainSearchFragment : Fragment() {
	var curPage = FRAG_RECENT
	var lResult : JsonArray? = null

	companion object {
		const val FRAG_RECENT = 0
		const val FRAG_RESULT = 1
		const val SP_SEARCH = "search"

		var frag : MainSearchFragment? = null

		fun getInstance(): MainSearchFragment {
			if (frag == null) frag = MainSearchFragment().apply { arguments = Bundle() }
			return frag!!
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.search_fragment, container, false)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val search_type_array = resources.getStringArray(R.array.search_type)
		val adapter = ArrayAdapter<String>(context!!, R.layout.search_type_spinner_item, search_type_array)
		adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
		search_type.adapter = adapter

		search_btn.setOnClickListener {
			search(search_type.selectedItemPosition, search_text.text.toString())
			val manager = (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
			manager.hideSoftInputFromWindow(search_text.windowToken, 0)
		}

		callPage(curPage)
	}

	fun search(type: Int, keyword: String) {
		val sp = SPHandler.getSp(context!!)
		val editor = sp.edit()

		val lRecent = JsonParser.parseString(sp.getString(SP_SEARCH, "[]")).asJsonArray
		for (i in 0..lRecent.size()-1) {
			val recentItem = lRecent[i].asString
			if (recentItem == keyword) {
				lRecent.remove(i)
				break
			}
		}
		lRecent.add(keyword)
		if (lRecent.size() > 5) lRecent.remove(0)
		editor.putString(SP_SEARCH, lRecent.toString())
		editor.apply()

		search_text.setText(keyword)

		val result: JsonObject
		val json = JsonObject()
		json.addProperty("keyword", keyword)
		result = when (type) {
			0 -> {
				ServerHandler.send(context, EndOfAPI.SEARCH_TOTAL, json)
			}
			else -> {
				ServerHandler.send(context, EndOfAPI.SEARCH_NAME, json)
			}
		}

		if (HttpHelper.isOK(result)) {
			lResult = result["data"].asJsonArray
			val frag = SearchResultFragment.getInstance()
			frag.keyword = keyword
			frag.lResult = lResult
			if (curPage == FRAG_RESULT) {
				frag.refresh()
			}
		} else {
			Toast.makeText(context, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
		}

		callPage(FRAG_RESULT)
	}

	override fun onDestroy() {
		super.onDestroy()

		val transaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
		transaction.remove(
			when(curPage) {
			FRAG_RECENT -> SearchRecentFragment.getInstance()
			else -> SearchResultFragment.getInstance()
		})
		transaction.commit()
	}

	fun callPage(no: Int) {
		val transaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()

		when (no) {
			FRAG_RECENT -> {
				transaction.replace(R.id.search_container, SearchRecentFragment.getInstance())
				transaction.commit()
			}

			FRAG_RESULT -> {
				transaction.replace(R.id.search_container, SearchResultFragment.getInstance())
				transaction.commit()
			}
		}

		curPage = no
	}
}