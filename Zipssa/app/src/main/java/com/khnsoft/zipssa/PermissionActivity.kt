package com.khnsoft.zipssa

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.permission_activity.*
import java.util.ArrayList

class PermissionActivity : AppCompatActivity() {
	companion object {
		const val SP_PERMISSION = "permission"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.permission_activity)

		val sp = SPHandler.getSp(this@PermissionActivity)
		val isPermissionChecked = sp.getBoolean(SP_PERMISSION, false)

		if (isPermissionChecked)
			requestPermission()

		permission_confirm_btn.setOnClickListener {
			val editor = sp.edit()
			editor.putBoolean(SP_PERMISSION, true)
			editor.apply()

			requestPermission()
		}
	}

	fun requestPermission() {
		TedPermission.with(this@PermissionActivity)
			.setPermissionListener(permissionListener)
			.setPermissions(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
			.check()
	}

	var permissionListener = object : PermissionListener {
		override fun onPermissionGranted() {
			val intent = Intent(this@PermissionActivity, LoginActivity::class.java)
			startActivity(intent)
			finish()
		}

		override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
			finish()
		}
	}
}
