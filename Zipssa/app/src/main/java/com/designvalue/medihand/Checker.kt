package com.designvalue.medihand

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