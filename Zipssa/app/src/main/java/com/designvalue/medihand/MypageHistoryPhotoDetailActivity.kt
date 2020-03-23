package com.designvalue.medihand

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.mypage_history_photo_detail_activity.*

class MypageHistoryPhotoDetailActivity : AppCompatActivity() {
	var curPhoto : Int = -1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_history_photo_detail_activity)

		val result = ServerHandler.send(this@MypageHistoryPhotoDetailActivity, EndOfAPI.GET_ALL_IMAGES)

		if (!HttpHelper.isOK(result)) {
			Toast.makeText(this@MypageHistoryPhotoDetailActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
			finish()
		}
		val lPhoto = result["data"].asJsonArray
		curPhoto = intent.getIntExtra(ExtraAttr.CUR_PHOTO, -1)

		val lm = LinearLayoutManager(this@MypageHistoryPhotoDetailActivity, LinearLayoutManager.HORIZONTAL, false)
		val adapter = PhotoRecyclerAdapter(lPhoto)
		detail_image_container.layoutManager = lm
		detail_image_container.adapter = adapter
	}

	fun showImage(lPhoto: JsonArray, curPhoto: Int) {
		for (i in 0..detail_image_container.size-1) {
			val holder = detail_image_container.findViewHolderForAdapterPosition(i) as PhotoRecyclerAdapter.ViewHolder

			if (i == curPhoto)
				holder.image.setBackgroundColor(getColor(R.color.main_theme))
			else
				holder.image.setBackgroundColor(Color.parseColor("#00000000"))
		}

		val jItem = ServerHandler.convertKeys(lPhoto[curPhoto].asJsonObject, ServerHandler.imageToLocal)
		image_item.setImageBitmap(ImageHelper.base64ToBitmap(jItem["image"].asString))
		created_date.text = SDF.dateDot.format(SDF.dateBar.parse(jItem["created_date"].asString))
	}

	inner class PhotoRecyclerAdapter(val lPhoto: JsonArray) :
		RecyclerView.Adapter<PhotoRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val image = itemView.findViewById<ImageView>(R.id.image_item)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(this@MypageHistoryPhotoDetailActivity).inflate(R.layout.mypage_history_photo_detail_list_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lPhoto.size()
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			val jItem = ServerHandler.convertKeys(lPhoto[position].asJsonObject, ServerHandler.imageToLocal)
			holder.image.setImageBitmap(ImageHelper.base64ToBitmap(jItem["image"].asString))
			holder.image.setOnClickListener {
				showImage(lPhoto, position)
			}

			if (position == curPhoto) {
				holder.image.setBackgroundColor(getColor(R.color.main_theme))
				image_item.setImageBitmap(ImageHelper.base64ToBitmap(jItem["image"].asString))
				created_date.text = SDF.dateDot.format(SDF.dateBar.parse(jItem["created_date"].asString))
			}
		}
	}
}
