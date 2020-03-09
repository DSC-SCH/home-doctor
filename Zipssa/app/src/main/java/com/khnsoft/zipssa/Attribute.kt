package com.khnsoft.zipssa

enum class HttpMethod(val method: String) {
    POST("POST"),
    GET("GET"),
    PUT("PUT"),
    DELETE("DELETE")
}

enum class EndOfAPI(val remote: String, val method: HttpMethod, val isIdNeeded: Boolean) {
    ADD_ALARM("/", HttpMethod.POST, false),
    EDIT_ALARM("/", HttpMethod.PUT, true),
    DELETE_ALARM("/", HttpMethod.DELETE, true)
}

class HttpAttr {
    companion object {
        const val SERVER_URL = "http://"
        const val ERROR_CODE = -1
        const val ERROR_MSG = """{"status": ${ERROR_CODE}}"""
    }
}

enum class AccountType(val type: String) {
    KAKAO("KAKAO"),
    GOOGLE("GOOGLE")
}

enum class AlarmStatus(val status: String) {
    ENABLED("ENABLE"),
    DISABLED("CANCEL")
}

enum class AlertType(val type: Int) {
    ALERT(0),
    CONFIRM(1)
}

enum class StatusCode(val status: Int) {
    SUCCESS(0),
    FAILED(1)
}