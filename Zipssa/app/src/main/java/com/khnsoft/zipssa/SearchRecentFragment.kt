package com.khnsoft.zipssa

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.main_activity.*

class SearchRecentFragment : Fragment() {
	lateinit var cur_context: MainSearchFragment

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.search_recent_fragment, container, false)
		return view
	}

	companion object {
		lateinit var frag: SearchRecentFragment

		fun getInstance(): SearchRecentFragment {
			if (!::frag.isInitialized) {
				frag = SearchRecentFragment().apply { arguments = Bundle() }
				return frag
			} else return frag
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		// TODO("Load currently searching history")
		// cur_context.search()
	}
}