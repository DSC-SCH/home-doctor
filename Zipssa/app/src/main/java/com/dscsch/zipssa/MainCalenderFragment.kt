package com.dscsch.zipssa

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class MainCalenderFragment : Fragment() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.main_calender, container, false)

		return view
	}

	companion object {
		val frag : MainCalenderFragment? = null

		fun newInstance() : MainCalenderFragment {
			return if (frag == null) MainCalenderFragment().apply {
				arguments = Bundle()
			} else frag
		}
	}
}