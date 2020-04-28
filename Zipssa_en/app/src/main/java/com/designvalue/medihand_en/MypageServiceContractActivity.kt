package com.designvalue.medihand_en

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.mypage_service_contract_activity.*

class MypageServiceContractActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.mypage_service_contract_activity)

		back_btn.setOnClickListener {
			onBackPressed()
		}

		val result = ServerHandler.send(this@MypageServiceContractActivity, EndOfAPI.GET_CONTRACTS)
		if (!HttpHelper.isOK(result)) {
			Toast.makeText(this@MypageServiceContractActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
			finish()
		}
		val lContract = result["data"].asJsonArray

		val adapter = ContractRecyclerAdapter(lContract)
		val lm = LinearLayoutManager(this@MypageServiceContractActivity)
		contract_container.layoutManager = lm
		contract_container.adapter = adapter
	}

	inner class ContractRecyclerAdapter(val lContract: JsonArray) :
		RecyclerView.Adapter<ContractRecyclerAdapter.ViewHolder>() {

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val title = itemView.findViewById<TextView>(R.id.contract_title)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(this@MypageServiceContractActivity).inflate(R.layout.mypage_service_contract_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lContract.size()
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			val jItem = lContract[position].asJsonObject

			holder.title.text = jItem["title"].asString

			holder.title.setOnClickListener {
				val intent = Intent(Intent.ACTION_VIEW)
				val uri = Uri.parse(jItem["content"].asString)
				intent.setData(uri)
				startActivity(intent)
			}
		}
	}
}
