package com.khnsoft.zipssa

class UserData {
    companion object {
        val DEFAULT_ACCOUNT = AccountType.NO_LOGIN
        const val DEFAULT_ID = -1

        var accountType: AccountType = DEFAULT_ACCOUNT
        var token: String? = null
        var id: Int = DEFAULT_ID
    }
}