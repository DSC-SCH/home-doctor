package com.khnsoft.zipssa

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.lang.Exception

enum class HttpMethod(val method: String) {
    POST("POST"),
    GET("GET"),
    PUT("PUT"),
    DELETE("DELETE")
}

enum class EndOfAPI(val remote: String, val method: HttpMethod, val isIdNeeded: Boolean) {
    // ONLINE only
    USER_REGISTER("/user", HttpMethod.POST, false),
    USER_LOGIN("/login", HttpMethod.POST, false),
    USER_GET("/user", HttpMethod.GET, false),

    // Both of OFFLINE and ONLINE
    GET_ENABLED_ALARMS("/alarm/enable", HttpMethod.GET, false),
    ADD_ALARM("/alarm/new", HttpMethod.POST, false),
    ADD_LABEL("/label/new", HttpMethod.POST, false),
    GET_LABELS("/label", HttpMethod.GET, false),
    EDIT_LABEL("/label", HttpMethod.PUT, true),
    DELETE_LABEL("/label", HttpMethod.DELETE, true),
    DELETE_ALARM("/alarm", HttpMethod.DELETE, true),
    EDIT_ALARM("/alarm", HttpMethod.PUT, true),
    GET_ALL_ALARMS("/alarm/all", HttpMethod.GET, false),

    USER_DELETE("/user", HttpMethod.DELETE, false),
    GET_ALARM("/alarm", HttpMethod.GET, true),
    ADD_IMAGE("/image/new", HttpMethod.POST, false),
    GET_IMAGES("/image", HttpMethod.POST, false),
    GET_ALL_IMAGE("/image/user", HttpMethod.POST, false),
    DELETE_IMAGE("/image", HttpMethod.DELETE, true),
    SYNC_GENERATE("/", HttpMethod.POST, false),
    SYNC_CONNECT("/", HttpMethod.POST, false),
    SYNC_GET_MANAGERS("/connect/manager", HttpMethod.GET, false),
    SYNC_GET_CREWS("/connect/receiver", HttpMethod.GET, false),
    SYNC_DELETE("/connect", HttpMethod.DELETE, true),
}

class HttpAttr {
    companion object {
        const val SERVER_URL = "http://"
        const val ERROR_CODE = -1
        const val OK_CODE = 200
        const val NO_USER_CODE = 401
        val ERROR_MSG = JsonParser.parseString("""{"status": ${ERROR_CODE}}""").asJsonObject
        val OK_MSG = JsonParser.parseString("""{"status": ${OK_CODE}}""").asJsonObject

        fun isOK(json: JsonObject) : Boolean{
            return try {
                json["status"].asInt == OK_CODE
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}

class AlarmParser {
    companion object {
        fun parseTimes(string: String) : JsonArray {
            val ret = string.split("/").joinToString { it -> "\"${it}\"" }
            return JsonParser.parseString("[${if (ret.equals("\"\"")) "" else ret}]").asJsonArray
        }

        fun parseRepeats(string: String) : JsonArray {
            return JsonParser.parseString("[${string.split("/").joinToString()}]").asJsonArray
        }

        fun parseStatus(string: String) : AlarmStatus {
            for (value in AlarmStatus.values()) {
                if (value.status.equals(string)) return value
            }
            return AlarmStatus.valueOf(string)
        }
    }
}

enum class AccountType(val type: String) {
    NO_LOGIN("NO_LOGIN"),
    NO_NETWORK("NO_NETWORK"),
    OFFLINE("OFFLINE"),
    KAKAO("KAKAO"),
    GOOGLE("GOOGLE")
}

enum class AlarmStatus(val status: String) {
    ENABLED("ENABLE"),
    DISABLED("CANCEL")
}

enum class AlertType() {
    ALERT,
    CONFIRM
}

enum class StatusCode(val status: Int) {
    SUCCESS(0),
    FAILED(1)
}

enum class Gender(val gender: String) {
    MEN("MEN"),
    WOMEN("WOMEN")
}

class Checker() {
    companion object {
        fun checkEmail(email: String) : Boolean {
            val etIdx = email.indexOf('@')
            val dotIdx = email.indexOf('.')

            return when {
                // No '@'
                etIdx == -1 -> false
                // No '.'
                dotIdx == -1 -> false
                // No letters before '@'
                etIdx == 0 -> false
                // No letters between '@' and '.'
                dotIdx == etIdx + 1 -> false
                // No letters after '.'
                email.length == dotIdx + 1 -> false
                else -> true
            }
        }

        fun checkPhone(phone: String) : String? {
            val elseStr = phone.replace("[0-9|-]".toRegex(), "")
            if (elseStr.isNotEmpty()) return null

            val onlyNum = phone.replace("[^0-9]".toRegex(), "")
            if (onlyNum.length < 8) return null

            return onlyNum
        }
    }
}

enum class PhotoAttr(val rc: Int) {
    CAMERA(301),
    GALLERY(300)
}