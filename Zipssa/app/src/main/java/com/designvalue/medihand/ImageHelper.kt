package com.designvalue.medihand

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

class ImageHelper {
	companion object {
		fun bitmapToBase64(bitmap: Bitmap) : String {
			val byteArrayOutputStream = ByteArrayOutputStream()
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
			return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
		}

		fun base64ToBitmap(base64: String) : Bitmap {
			val byteArray = Base64.decode(base64, Base64.DEFAULT)
			return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
		}
	}
}