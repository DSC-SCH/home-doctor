package com.khnsoft.zipssa

import android.util.Log
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.lang.Exception

class HttpHelper {
    companion object {
        const val SERVER_URL = "http://175.126.196.153:8081"
        const val ERROR_CODE = -1
        const val OK_CODE = 200
        const val NO_USER_CODE = 401
        private val ERROR_MSG: JsonObject = JsonObject()
        private val OK_MSG: JsonObject = JsonObject()

        init {
            ERROR_MSG.addProperty("status", ERROR_CODE)
            OK_MSG.addProperty("status", OK_CODE)
            OK_MSG.addProperty("message", "내부 서버 실행 성공")
        }

        fun isOK(json: JsonObject) : Boolean{
            return try {
                json["status"].asInt == OK_CODE
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        fun getError(msg: String? = null) : JsonObject {
            val ret = ERROR_MSG.deepCopy()
            if (msg == null) {
                ret.addProperty("message", "오류 발생")
            } else {
                ret.addProperty("message", msg)
            }
            MyLogger.e("Execute Error", ret.toString())
            return ret
        }

        fun getOK(data : JsonElement? = null) : JsonObject {
            val ret = OK_MSG.deepCopy()
            ret.add("data", data)
            return ret
        }

        fun getOK(data : String) : JsonObject {
            val ret = OK_MSG.deepCopy()
            ret.addProperty("data", data)
            return ret
        }

        fun getOK(data : Int) : JsonObject {
            val ret = OK_MSG.deepCopy()
            ret.addProperty("data", data)
            return ret
        }
    }
}