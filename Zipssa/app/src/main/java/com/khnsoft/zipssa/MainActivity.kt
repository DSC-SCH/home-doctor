package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {
	val FRAG_HOME = 0
	val FRAG_SEARCH = 2

	var buttons= List<LinearLayout>(0) { LinearLayout(this@MainActivity) }
	var cur_frag = -1;

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		buttons = listOf(bar_home, bar_add, bar_search, bar_mypage)

		bar_home.setOnClickListener {
			callPage(FRAG_HOME)
		}

		bar_add.setOnClickListener {
			val intent = Intent(this@MainActivity, AddAlarmActivity::class.java)
			startActivity(intent)
		}

		bar_search.setOnClickListener {
			callPage(FRAG_SEARCH)
		}

		bar_mypage.setOnClickListener {
			val intent = Intent(this@MainActivity, MypageActivity::class.java)
			startActivity(intent)
		}

		callPage(FRAG_HOME)
	}

	fun callPage(no: Int) {
		val transaction = supportFragmentManager.beginTransaction()

		for (button in buttons) {
			button.setBackgroundColor(0)
		}

		when (no) {
			FRAG_HOME -> {
				transaction.replace(R.id.main_container, MainListFragment.getInstance())
				transaction.commit()
			}

			FRAG_SEARCH -> {
				transaction.replace(R.id.main_container, MainSearchFragment.getInstance())
				transaction.commit()
			}
		}

		buttons[no].setBackgroundColor(getColor(R.color.main_bottom_bar_selected))
		cur_frag = no
	}

	override fun onBackPressed() {
		if (cur_frag == FRAG_HOME)
			super.onBackPressed()
		else
			callPage(FRAG_HOME)
	}
}