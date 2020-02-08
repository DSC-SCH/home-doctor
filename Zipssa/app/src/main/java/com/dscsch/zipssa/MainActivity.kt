package com.dscsch.zipssa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {
	val DB_VERSION = 1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		SharedDB.helper = DBHelper(this@MainActivity, SharedDB.DB_NAME, null, DB_VERSION)
		SharedDB.database = SharedDB.helper.writableDatabase

		val adapterViewPager = MyPageAdapter(supportFragmentManager)
		main_pager.adapter = adapterViewPager
	}

	class MyPageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
		private val NUM_ITEMS = 3
		private var frag = null

		override fun getItem(position: Int): Fragment {
			return when(position) {
				1 -> MainTimerFragment.getInstance()
				2 -> MainCalenderFragment.getInstance()
				else -> MainListFragment.getInstance()
			}
		}

		override fun getCount(): Int {
			return NUM_ITEMS
		}
	}
}