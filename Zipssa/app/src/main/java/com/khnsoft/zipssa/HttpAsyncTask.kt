package com.khnsoft.zipssa

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class HttpAsyncTask : AsyncTask<String, Void, String>() {
	companion object {
		const val SERVER_URL = "http://"
		const val ERROR_CODE = -1
		const val ERROR_MSG = """{"status": ${ERROR_CODE}}"""

		const val POST = "POST"
		const val GET = "GET"
		const val PUT = "PUT"
		const val DELETE = "DELETE"


	}

	/**
	 * @param remote End of API URL
	 * @param method One of {POST, GET, PUT, DELETE}
	 * @param sMsg Json data in String (optional, only for POST and PUT)
	 */
	override fun doInBackground(vararg params: String?): String {
		if (params.size < 2)
			return ERROR_MSG

		val remote = params[0]
		val method = params[1]
		val sMsg: String?

		if (remote == null || method == null)
			return ERROR_MSG

		if (method == POST || method == PUT) {
			if (params.size < 3)
				return ERROR_MSG
			sMsg = params[2]
		} else if (method == GET || method == DELETE) {
			sMsg = null
		} else
			return ERROR_MSG

		return getResultFromAPI(remote, method, sMsg)
	}

	fun getResultFromAPI(remote: String, method: String, sMsg: String?) : String{
		var ret = ERROR_MSG
		var httpCon: HttpURLConnection? = null

		try {
			val url = URL("${SERVER_URL}${remote}")
			httpCon = url.openConnection() as HttpURLConnection

			httpCon.requestMethod = method
			httpCon.setRequestProperty("Content-type", "application/json")
			httpCon.setRequestProperty("Accept", "*/*")
			httpCon.doInput = true

			if (method == POST || method == PUT) {
				if (sMsg == null)
					return ERROR_MSG

				val outputStream = httpCon.outputStream
				outputStream.write(sMsg.toByteArray())
				outputStream.flush()
			}

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