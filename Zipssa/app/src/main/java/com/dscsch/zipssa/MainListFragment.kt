package com.dscsch.zipssa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class MainListFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.main_list_fragment, container, false)

		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val cur_context = view.context

		super.onViewCreated(view, savedInstanceState)
	}

	companion object {
		lateinit var frag : MainListFragment

		fun getInstance() : MainListFragment {
			if (!::frag.isInitialized) {
				frag = MainListFragment().apply { arguments = Bundle() }
				return frag
			} else return frag
		}
	}
}