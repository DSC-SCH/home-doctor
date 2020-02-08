package com.dscsch.zipssa

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.main_list.*

class MainListFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.main_list, container, false)

		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val cur_context = view.context

		add_button.setOnClickListener {
			val nextIntent = Intent(cur_context, AddAlarm::class.java)
			startActivity(nextIntent)
		}

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