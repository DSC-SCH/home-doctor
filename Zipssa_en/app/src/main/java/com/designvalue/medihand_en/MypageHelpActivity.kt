package com.designvalue.medihand_en

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.mypage_help_activity.*

class MypageHelpActivity : AppCompatActivity() {

	val images = arrayOf(
		R.drawable.help_1,
		R.drawable.help_2,
		R.drawable.help_3,
		R.drawable.help_4,
		R.drawable.help_5,
		R.drawable.help_6,
		R.drawable.help_7,
		R.drawable.help_8,
		R.drawable.help_9,
		R.drawable.help_10
	)
	var curPage = 0

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_help_activity)

		help_prev_btn.setOnClickListener {
			curPage--
			setPage()
		}

		setPage()
	}

	fun setPage() {
		help_image.setImageResource(images[curPage])
		help_page.text = (curPage + 1).toString()

		if (curPage == 0) {
			help_prev_btn.visibility = View.GONE
		} else {
			help_prev_btn.visibility = View.VISIBLE
		}

		if (curPage == images.size - 1) {
			help_next_btn.text = "Close"
			help_next_btn.setOnClickListener {
				finish()
			}
		} else {
			help_next_btn.text = "Next"
			help_next_btn.setOnClickListener {
				curPage++
				setPage()
			}
		}
	}
}
