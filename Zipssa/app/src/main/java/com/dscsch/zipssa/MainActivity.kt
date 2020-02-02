package com.dscsch.zipssa

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		val adapterViewPager = MyPageAdapter(supportFragmentManager)
		main_pager.adapter = adapterViewPager

		val nextIntent = Intent(this, AddAlarm::class.java)
		startActivity(nextIntent)
	}

	class MyPageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
		private val NUM_ITEMS = 3
		private var frag = null

		override fun getItem(position: Int): Fragment {
			return when(position) {
				1 -> MainTimerFragment.newInstance()
				2 -> MainCalenderFragment.newInstance()
				else -> MainListFragment.newInstance()
			}
		}

		override fun getCount(): Int {
			return NUM_ITEMS
		}
	}
}