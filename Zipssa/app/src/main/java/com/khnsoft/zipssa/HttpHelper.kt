package com.khnsoft.zipssa

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
            ERROR_MSG.addProperty("message", "내부 서버 실행 오류")
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

        fun getError() : JsonObject {
            return ERROR_MSG
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