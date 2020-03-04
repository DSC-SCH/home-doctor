package com.khnsoft.zipssa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.mypage_sync_activity.*

class MypageSyncActivity : AppCompatActivity() {
	val PAGE_MANAGER = 0
	val PAGE_CREW = 1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_sync_activity)

		back_btn.setOnClickListener { onBackPressed() }

		val radioGroup = MyRadioGroup()
		radioGroup.add(manage_manager)
		radioGroup.add(manage_crew)

		// TODO("Validation code after deadline")
	}

	fun changePage(page: Int) {
		when (page) {
			PAGE_MANAGER -> {

			}

			PAGE_CREW -> {

			}

			else -> return
		}
	}
}
