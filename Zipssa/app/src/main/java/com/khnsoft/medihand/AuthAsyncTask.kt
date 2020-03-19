package com.khnsoft.medihand

import android.os.AsyncTask
import com.google.gson.JsonObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class AuthAsyncTask : AsyncTask<String, Void, String>() {
	companion object {
		const val ADDR_TOKEN = "https://api.iamport.kr/users/getToken"
		const val ADDR_CERTIFY = "https://api.iamport.kr/certifications"
	}

	/**
	 * @param params (AuthMethod.GET_TOKEN, imp_key, imp_secret)
	 * @param params (AuthMethod.CERTIFICATION, imp_token, imp_uid)
	 */
	override fun doInBackground(vararg params: String?): String {
		try {
			val method = AuthMethod.valueOf(params[0]!!)
			if (method==AuthMethod.GET_TOKEN)
				return getToken(params[1]!!, params[2]!!)
			else if (method == AuthMethod.CERTIFICATION)
				return certify(params[1]!!, params[2]!!)
		} catch (e: Exception) {
			e.printStackTrace()
		}
		return HttpHelper.getError(HttpError.ASYNC_METHOD).toString()
	}

	fun getToken(impKey: String, impSecret: String) : String{
		var ret : String? = null
		var httpCon: HttpURLConnection? = null

		try {
			val url = URL(ADDR_TOKEN)
			httpCon = url.openConnection() as HttpURLConnection

			httpCon.requestMethod = "POST"
			httpCon.connectTimeout = 3000
			httpCon.setRequestProperty("Content-Type", "application/json")
			httpCon.doInput = true

			val json = JsonObject()
			json.addProperty("imp_key", impKey)
			json.addProperty("imp_secret", impSecret)

			val outputStream = httpCon.outputStream
			outputStream.write(json.toString().toByteArray())
			outputStream.flush()

			val status = httpCon.responseCode
			var inputStream: InputStream? = null
			try {
				if (status != HttpURLConnection.HTTP_OK)
					inputStream = httpCon.errorStream
				else
					inputStream = httpCon.inputStream

				if (inputStream != null)
					ret = convertInputStreamToString(inputStream)
			} catch (e: Exception) {
				e.printStackTrace()
			}
		} catch (e: Exception) {
			e.printStackTrace()
		} finally {
			httpCon?.disconnect()
		}

		if (ret == null) ret = HttpHelper.getError(HttpError.AUTH_TOKEN).toString()
		MyLogger.d("HttpInput", ret)
		return ret
	}

	fun certify(impToken: String, impUid: String): String {
		var ret : String? = null
		var httpCon: HttpURLConnection? = null

		try {
			val url = URL("${ADDR_CERTIFY}/${impUid}")
			httpCon = url.openConnection() as HttpURLConnection

			httpCon.requestMethod = "GET"
			httpCon.connectTimeout = 3000
			httpCon.setRequestProperty("Content-Type", "application/json")
			httpCon.setRequestProperty("Authorization", impToken)
			httpCon.doInput = true

			val status = httpCon.responseCode
			var inputStream: InputStream? = null
			try {
				if (status != HttpURLConnection.HTTP_OK)
					inputStream = httpCon.errorStream
				else
					inputStream = httpCon.inputStream

				if (inputStream != null)
					ret = convertInputStreamToString(inputStream)
			} catch (e: Exception) {
				e.printStackTrace()
			}
		} catch (e: Exception) {
			e.printStackTrace()
		} finally {
			httpCon?.disconnect()
		}

		if (ret == null) ret = HttpHelper.getError(HttpError.AUTH_TOKEN).toString()
		MyLogger.d("HttpInput", ret)
		return ret
	}

	@Throws(IOException::class)
	fun convertInputStreamToString(inputStream: InputStream): String {
		val bufferedReader = BufferedReader(InputStreamReader(inputStream))
		var ret = ""

		var line = bufferedReader.readLine()
		while (line != null) {
			ret += line
			line = bufferedReader.readLine()
		}
		inputStream.close()
		return ret
	}
}