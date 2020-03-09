package com.khnsoft.zipssa

enum class HttpMethod(val method: String) {
    POST("POST"),
    GET("GET"),
    PUT("PUT"),
    DELETE("DELETE")
}

enum class EndOfAPI(val remote: String, val method: HttpMethod) {
    ADD_ALARM("/", HttpMethod.POST)
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