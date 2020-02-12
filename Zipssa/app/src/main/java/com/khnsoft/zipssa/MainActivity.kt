package com.khnsoft.zipssa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {
	val DB_VERSION = 1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		bar_home.setOnClickListener {
			callPage(0)
		}

		bar_add.setOnClickListener {
			val intent = Intent(this@MainActivity, AddAlarm::class.java)
			startActivity(intent)
		}

		callPage(0)
	}

	fun callPage(no: Int) {
		val transaction = supportFragmentManager.beginTransaction()

		when (no) {
			0 -> {
				transaction.replace(R.id.main_container, MainListFragment.getInstance())
				transaction.commit()
			}
		}
	}
}