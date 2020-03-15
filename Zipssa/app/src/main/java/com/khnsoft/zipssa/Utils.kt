package com.khnsoft.zipssa

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

enum class HttpMethod(val method: String) {
    POST("POST"),
    GET("GET"),
    PUT("PUT"),
    DELETE("DELETE")
}

enum class EndOfAPI(val remote: String, val method: HttpMethod, val isIdNeeded: Boolean = false, val isId2Needed: Boolean = false) {
    // ONLINE only
    OFFLINE_USER_REGISTER("/user", HttpMethod.POST),
    USER_REGISTER("/user", HttpMethod.POST),
    USER_LOGIN("/login", HttpMethod.POST),
    USER_GET("/user", HttpMethod.GET),
    GET_CONTRACTS("/terms", HttpMethod.GET),

    SYNC_GENERATE("/code", HttpMethod.POST),
    SYNC_CONNECT("/connect/new", HttpMethod.POST),
    SYNC_GET_MANAGERS("/connect/manager", HttpMethod.GET),
    SYNC_GET_CREWS("/connect/receiver", HttpMethod.GET),
    SYNC_DELETE("/connect", HttpMethod.DELETE, true),
    SYNC_GET_ALL_ALARMS("/connect/receiver/alarm", HttpMethod.GET, true),
    SYNC_GET_ALARM("/connect/receiver/alarm", HttpMethod.GET, true, true),
    SYNC_EDIT_ALARM("/connect/receiver/alarm", HttpMethod.PUT, true, true),
    SYNC_DELETE_ALARM("/connect/receiver/alarm", HttpMethod.DELETE, true, true),
    SYNC_ADD_ALARM("/connect/receiver/alarm", HttpMethod.POST, true),

    ENQUIRE("/", HttpMethod.POST),

    // Both of OFFLINE and ONLINE
    ADD_ALARM("/alarm/new", HttpMethod.POST),
    EDIT_ALARM("/alarm", HttpMethod.PUT, true),
    DELETE_ALARM("/alarm", HttpMethod.DELETE, true),
    GET_ALARM("/alarm", HttpMethod.GET, true),
    GET_ENABLED_ALARMS("/alarm/enable", HttpMethod.GET),
    GET_ALL_ALARMS("/alarm/all", HttpMethod.GET),
    CHANGE_ALARM_STATE("/alarm/change", HttpMethod.PUT, true),
    ADD_LABEL("/label/new", HttpMethod.POST),
    GET_LABELS("/label", HttpMethod.GET),
    EDIT_LABEL("/label", HttpMethod.PUT, true),
    DELETE_LABEL("/label", HttpMethod.DELETE, true),
    ADD_IMAGE("/image", HttpMethod.POST, true),
    GET_IMAGES("/image/alarm", HttpMethod.GET, true),
    EDIT_IMAGES("/image", HttpMethod.PUT, true),
    GET_ALL_IMAGES("/image/user", HttpMethod.GET),

    USER_DELETE("/user", HttpMethod.DELETE),
    DELETE_IMAGE("/image", HttpMethod.DELETE, true),
    GET_COUNT_DATE("/", HttpMethod.POST, false),
    GET_COUNT("/", HttpMethod.GET, true),
    GET_COUNT_ALARM("/", HttpMethod.GET, true),
    SYNC_GET_LABELS("/", HttpMethod.GET, true),
    GET_NOTICES("/", HttpMethod.GET),
    SEARCH("/", HttpMethod.POST),
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

class ExtraAttr {
    companion object {
        const val ALARM_ID = "alarm_id"
        const val CUR_PHOTO = "cur_photo"
        const val CONTRACT_NUM = "contract_num"
        const val CONTRACT = "contract"
        const val CONTRACT_CHECKED = "checked"
        const val LABEL = "label"
        const val POPUP_DATA = "data_id"
        const val POPUP_RESULT = "popup_result"
        const val CALENDAR_DATE = "date"
        const val CALENDAR_START = "calendarstart"
        const val MEDICINE = "medicine"
    }
}

enum class Gender(val gender: String) {
    MEN("MEN"),
    WOMEN("WOMEN")
}

enum class PhotoAttr(val rc: Int) {
    CAMERA(301),
    GALLERY(300)
}

enum class StatusCode(val status: Int) {
    SUCCESS(0),
    FAILED(1)
}

class SPHandler {
    companion object {
        const val SP_NAME = "MediHand"

        fun getSp(context: Context) : SharedPreferences {
            val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            return sp
        }
    }
}

class HttpError {
    companion object {
        const val METHOD_NO_DATA = "No data for POST or PUT method"
        const val METHOD_ERROR = "Given method is wrong"
        const val NO_ID1 = "Id1 is needed but not given"
        const val NO_ID2 = "Id2 is needed but not given"
        const val NO_CONTEXT = "No context is given for offline"
    }
}

enum class LogType {
    INFO,
    ERROR,
    DEBUG,
}

class SDF {
    companion object {
        val dateBar = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        val dateInKorean = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
        val dateDotShort = SimpleDateFormat("yy.MM.dd", Locale.KOREA)
        val dateDot = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        val dateSlash = SimpleDateFormat("yyyy / MM / dd", Locale.KOREA)

        val time = SimpleDateFormat("HH:mm", Locale.KOREA)
        val timeInEnglish = SimpleDateFormat("a h:mm", Locale.ENGLISH)
        val timeInKorean = SimpleDateFormat("a h:mm", Locale.KOREA)

        val yearMonth = SimpleDateFormat("yyyy-MM", Locale.KOREA)
        val monthDateInKorean = SimpleDateFormat("M월 d일", Locale.KOREA)
        val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)
    }
}