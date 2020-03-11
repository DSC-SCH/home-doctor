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

	/**
	 * @param params (end of api url, method, json data in string (for POST and PUT))
	 */
	override fun doInBackground(vararg params: String?): String {
		return getResultFromAPI(params[0], HttpMethod.valueOf(params[1] as String), params[2])
	}

	fun getResultFromAPI(remote: String?, method: HttpMethod, sMsg: String?) : String{
		if (remote == null) return HttpAttr.ERROR_MSG.toString()

		var ret = HttpAttr.ERROR_MSG.toString()
		var httpCon: HttpURLConnection? = null

		try {
			val url = URL("${HttpAttr.SERVER_URL}${remote}")
			httpCon = url.openConnection() as HttpURLConnection

			httpCon.requestMethod = method.method
			httpCon.setRequestProperty("Content-Type", "application/json")
			httpCon.setRequestProperty("Authorization", UserData.token)
			httpCon.doInput = true

			if (method == HttpMethod.POST || method == HttpMethod.PUT) {
				if (sMsg == null)
					return HttpAttr.ERROR_MSG.toString()

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