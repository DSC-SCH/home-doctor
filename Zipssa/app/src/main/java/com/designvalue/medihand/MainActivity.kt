package com.designvalue.medihand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.list_fragment.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.search_fragment.*

class MainActivity : AppCompatActivity() {
	val FRAG_HOME = 0
	val FRAG_SEARCH = 2

	var buttons= List<LinearLayout>(0) { LinearLayout(this@MainActivity) }
	var cur_frag = FRAG_HOME

	companion object {
		var curActivity : MainActivity? = null
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		startLoading()

		curActivity = this@MainActivity

		buttons = listOf(bar_home, bar_add, bar_search, bar_mypage)

		bar_home.setOnClickListener {
			callPage(FRAG_HOME)
		}

		bar_add.setOnClickListener {
			if (UserData.accountType == AccountType.NO_NETWORK) {
				MyAlertPopup.needNetwork(this@MainActivity)
				return@setOnClickListener
			}
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

		callPage(cur_frag)
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
		if (cur_frag == FRAG_HOME) {
			val frag = MainListFragment.getInstance()
			if (frag.sync_drawer.isDrawerOpen(Gravity.RIGHT)) {
				frag.sync_drawer.closeDrawer(Gravity.RIGHT)
			} else {
				finishAffinity()
				System.runFinalization()
				System.exit(0)
			}
		} else if (MainSearchFragment.getInstance().curPage == MainSearchFragment.FRAG_RESULT) {
			MainSearchFragment.getInstance().callPage(MainSearchFragment.FRAG_RECENT)
			MainSearchFragment.getInstance().search_text.setText("")
		} else {
			callPage(FRAG_HOME)
		}
	}

	fun startLoading() {
		loading.visibility = View.VISIBLE
	}

	fun endLoading() {
		loading.visibility = View.GONE
	}

	override fun onDestroy() {
		super.onDestroy()

		val transaction = supportFragmentManager.beginTransaction()
		transaction.remove(
			when (cur_frag) {
				FRAG_HOME -> MainListFragment.getInstance()
				else -> MainSearchFragment.getInstance()
			}
		)
		transaction.commit()
	}
}