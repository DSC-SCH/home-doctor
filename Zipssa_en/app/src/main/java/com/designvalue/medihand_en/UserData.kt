package com.designvalue.medihand_en

class UserData {
    companion object {
        val DEFAULT_ACCOUNT = AccountType.NO_LOGIN
        const val DEFAULT_ID = -1

        var accountType: AccountType = DEFAULT_ACCOUNT
        var token: String? = null
        var id: Int = DEFAULT_ID
        var name: String = "John Doe"
        var careName: String? = null
        var careUser : Int? = null
        var version : String = ""

        fun isOnline() : Boolean {
            return accountType == AccountType.GOOGLE || accountType == AccountType.KAKAO
        }

        fun isOffline() : Boolean {
            return accountType == AccountType.NO_NETWORK || accountType == AccountType.OFFLINE
        }
    }
}