package com.khnsoft.zipssa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MypageSyncActivity : AppCompatActivity() {
	val PAGE_MANAGER = 0
	val PAGE_CREW = 1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_sync_activity)

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
