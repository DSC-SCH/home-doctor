package com.dscsch.zipssa

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class MainTimerFragment : Fragment() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.main_timer, container, false)

		return view
	}

	companion object {
		lateinit var frag : MainTimerFragment

		fun getInstance() : MainTimerFragment {
			if (!::frag.isInitialized) {
				frag = MainTimerFragment().apply { arguments = Bundle() }
				return frag
			} else return frag
		}
	}
}