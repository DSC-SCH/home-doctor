package com.khnsoft.zipssa

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
    GET_CONTRACTS("/terms", HttpMethod.GET, false),
    SYNC_GENERATE("/", HttpMethod.POST, false),
    SYNC_CONNECT("/", HttpMethod.POST, false),
    SYNC_GET_MANAGERS("/connect/manager", HttpMethod.GET, false),
    SYNC_GET_CREWS("/connect/receiver", HttpMethod.GET, false),
    SYNC_DELETE("/connect", HttpMethod.DELETE, true),
    SYNC_GET_ALARMS("/connect/receiver/alarm", HttpMethod.DELETE, true),

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
    CHANGE_ALARM("/alarm/change", HttpMethod.PUT, true),
    GET_ALARM("/alarm", HttpMethod.GET, true),
    ADD_IMAGE("/image/new", HttpMethod.POST, true),
    GET_IMAGES("/image/alarm", HttpMethod.GET, true),
    EDIT_IMAGES("/image/edit", HttpMethod.POST, true),

    USER_DELETE("/user", HttpMethod.DELETE, false),
    GET_ALL_IMAGE("/image/user", HttpMethod.POST, false),
    DELETE_IMAGE("/image", HttpMethod.DELETE, true),
    GET_SYNC_ALARMS("/connect/receiver/alarm", HttpMethod.DELETE, true),
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

enum class ExtraAttr(val extra: String) {
    EXTRA_ALARM_ID("alarm_id"),
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

class UserData {
    companion object {
        val DEFAULT_ACCOUNT = AccountType.NO_LOGIN
        const val DEFAULT_ID = -1

        var accountType: AccountType = DEFAULT_ACCOUNT
        var token: String? = null
        var id: Int = DEFAULT_ID
    }
}

class SharedPreferencesSrc {
    companion object {
        const val SP_NAME = "account"
    }
}