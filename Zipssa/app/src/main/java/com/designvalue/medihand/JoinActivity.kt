package com.designvalue.medihand

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.account_join_activity.*
import java.util.*

class JoinActivity : AppCompatActivity() {
	companion object {
		const val RC_IDENTIFY = 100

		const val BIRTHDAY_NO_SELECT = -1L
		const val BIRTHDAY_AGE = -2L
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.account_join_activity)

		/*
		val identifyIntent = Intent(this@JoinActivity, AuthWebActivity::class.java)
		startActivityForResult(identifyIntent, RC_IDENTIFY)
		 */

		val _name = intent.getStringExtra("name")
		val _gender = intent.getStringExtra("gender")
		val _email = intent.getStringExtra("email")
		val _sns_type = intent.getStringExtra("sns_type")
		val _sns_id = intent.getStringExtra("sns_id")

		name_input.setText(_name?:"")
		email_input.setText(_email?:"")
		when (_gender) {
			Gender.MEN.name -> gender_group.check(R.id.gender_m)
			Gender.WOMEN.name -> gender_group.check(R.id.gender_f)
			else -> {}
		}

		val result = ServerHandler.send(this@JoinActivity, EndOfAPI.GET_CONTRACTS)
		if (!HttpHelper.isOK(result)) {
			Toast.makeText(this@JoinActivity, result["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
			finish()
		}
		val lContract = result["data"].asJsonArray

		val adapter = ContractRecyclerAdapter(lContract)
		val lm = LinearLayoutManager(this@JoinActivity)
		contract_container.layoutManager = lm
		contract_container.adapter = adapter

		val curCal = Calendar.getInstance()
		var birthdayTime = BIRTHDAY_NO_SELECT

		val birthdayListener = View.OnClickListener {
			if (birthdayTime != BIRTHDAY_NO_SELECT) curCal.time = SDF.dateInKorean.parse(birthday_input.text.toString())
			DatePickerDialog(this@JoinActivity, DatePickerDialog.THEME_HOLO_LIGHT, { view, year, month, dayOfMonth ->
				curCal[Calendar.YEAR] = year
				curCal[Calendar.MONTH] = month
				curCal[Calendar.DAY_OF_MONTH] = dayOfMonth
				birthday_input.text = SDF.dateInKorean.format(curCal.time)
				curCal.add(Calendar.YEAR, 14)
				birthdayTime = curCal.timeInMillis
			}, curCal[Calendar.YEAR], curCal[Calendar.MONTH], curCal[Calendar.DAY_OF_MONTH]).show()
		}

		birthday_input.setOnClickListener(birthdayListener)
		birthday_select.setOnClickListener(birthdayListener)

		join_confirm_btn.setOnClickListener {
			var isOK = true
			if (name_input.text.isBlank()) {
				isOK = false
				join_name_check_warning.text = "이름을 입력해주세요"
			} else {
				join_name_check_warning.text = ""
			}

			if (!gender_m.isChecked && !gender_f.isChecked) {
				isOK = false
				join_gender_check_warning.text = "성별을 선택해주세요"
			} else {
				join_gender_check_warning.text = ""
			}

			if (birthdayTime == BIRTHDAY_NO_SELECT) {
				isOK = false
				join_birthday_check_warning.text = "생년월일을 입력해주세요"
			} else if (birthdayTime > System.currentTimeMillis()) {
				isOK = false
				join_birthday_check_warning.text = "14세 미만의 청소년은 가입이 제한됩니다"
			} else {
				join_birthday_check_warning.text = ""
			}

			if (!Checker.checkEmail(email_input.text.toString())) {
				isOK = false
				join_email_check_warning.text = "정확한 이메일을 입력해주세요"
			} else {
				join_email_check_warning.text = ""
			}

			val validPh = Checker.checkPhone(ph_input.text.toString())
			if (validPh == null) {
				isOK = false
				join_ph_warning.text = "정확한 전화번호를 입력해주세요"
			} else {
				join_ph_warning.text = ""
			}

			// Check contracts

			if (!checkContracts()) {
				isOK = false
				join_contract_warning.text = "모든 약관을 동의하셔야 가입이 진행됩니다"
			} else {
				join_contract_warning.text = ""
			}

			if (isOK) {
				val json = JsonObject()
				json.addProperty("username", name_input.text.toString())
				json.addProperty("birthday", SDF.dateBar.format(SDF.dateInKorean.parse(birthday_input.text.toString())))
				json.addProperty("email", email_input.text.toString())
				json.addProperty("snsType", _sns_type)
				json.addProperty("snsId", _sns_id)
				json.addProperty("genderType", if (gender_m.isChecked) Gender.MEN.name else Gender.WOMEN.name)
				json.addProperty("phoneNum", validPh)

				val result2 = ServerHandler.send(this@JoinActivity, EndOfAPI.USER_REGISTER, json)

				if (!HttpHelper.isOK(result2)) {
					Toast.makeText(this@JoinActivity, result2["message"]?.asString ?: "null", Toast.LENGTH_SHORT).show()
					return@setOnClickListener
				}

				val data = MyAlertPopup.Data(AlertType.CONFIRM).apply {
					alertTitle = "가입 성공"
					alertContent = "서비스에 가입되었습니다."
				}
				val dataId = DataPasser.insert(data)
				val intent = Intent(this@JoinActivity, MyAlertPopup::class.java)
				intent.putExtra(ExtraAttr.POPUP_DATA, dataId)
				startActivity(intent)

				val sp = SPHandler.getSp(this@JoinActivity)
				val editor = sp.edit()
				editor.putString(LoginActivity.SP_LOGIN, _sns_type)
				editor.apply()

				val userData = result2["data"].asJsonObject
				UserData.accountType = AccountType.valueOf(_sns_type)
				UserData.id = userData["userId"].asInt
				UserData.token = userData["token"].asString
				UserData.name = name_input.text.toString()

				val intent2 = Intent(this@JoinActivity, LoadingActivity::class.java)
				startActivity(intent2)
				LoginActivity.curActivity?.finish()
				finish()
			}
		}
	}

	fun checkContracts() : Boolean {
		val adapter = contract_container.adapter as ContractRecyclerAdapter
		val checkes = adapter.curCheckes
		for (i in 0..checkes.size-1) {
			if (!checkes[i]) {
				return false
			}
		}

		return true
	}

	inner class ContractRecyclerAdapter(val lContract: JsonArray) :
		RecyclerView.Adapter<ContractRecyclerAdapter.ViewHolder>() {
		val curCheckes = MutableList(lContract.size()) {false}

		inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			val title = itemView.findViewById<CheckBox>(R.id.contract_title)
			val content = itemView.findViewById<TextView>(R.id.contract_content)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
			val view = LayoutInflater.from(this@JoinActivity).inflate(R.layout.account_join_contract_item, parent, false)
			return ViewHolder(view)
		}

		override fun getItemCount(): Int {
			return lContract.size()
		}

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			val jItem = lContract[position].asJsonObject

			holder.title.text = jItem["title"].asString
			holder.title.setOnCheckedChangeListener { buttonView, isChecked ->
				curCheckes.set(position, isChecked)
			}
			holder.content.setOnClickListener {
				val intent = Intent(Intent.ACTION_VIEW)
				val uri = Uri.parse(jItem["content"].asString)
				intent.setData(uri)
				startActivity(intent)
			}
		}
	}

	/*
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == ContractActivity.RC_CONTRACT && data != null) {
			val holder = contract_container.findViewHolderForAdapterPosition(data!!.getIntExtra(ExtraAttr.CONTRACT_NUM, -1)) as ContractRecyclerAdapter.ViewHolder
			holder.title.isChecked = data.getBooleanExtra(ExtraAttr.CONTRACT_CHECKED, false)
		} else if (requestCode == RC_IDENTIFY) {
			if (data == null) finish()
			MyLogger.d("@@@", "RECEIVED DATA")
		}
	}
	 */
}
