package com.dscsch.zipssa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.add_alarm_activity.*

class AddAlarm : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.add_alarm_activity)

		setSupportActionBar(add_toolbar)

		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)

		// Setting alarm count
		val alarm_counts_array = resources.getStringArray(R.array.alarm_times)
		val adapter = ArrayAdapter<String>(applicationContext, R.layout.alarm_count_spinner_item, alarm_counts_array)
		adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
		alarm_counts.adapter = adapter

		alarm_counts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onNothingSelected(parent: AdapterView<*>?) {
				return
			}

			override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
				return
			}
		}

		submit_button.setOnClickListener {

		}
	}

	// Setting buttons on title bar
	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.home, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.action_home -> {
				finish()
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}

	override fun onSupportNavigateUp(): Boolean {
		onBackPressed()
		return true
	}
}
