package com.designvalue.medihand

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

		/*

		try {
			val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
			for (signature in info.signatures) {
				val md = MessageDigest.getInstance("SHA")
				md.update(signature.toByteArray())
				MyLogger.i("@@@@@@", Base64.encodeToString(md.digest(), Base64.DEFAULT))
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
		// */

		UserData.version = packageManager.getPackageInfo(packageName, 0).versionName

		val intent = Intent(this@PermissionActivity, LoginActivity::class.java)
		startActivity(intent)
		finish()

		return
		// */

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
			.setPermissions(android.Manifest.permission.INTERNET, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
