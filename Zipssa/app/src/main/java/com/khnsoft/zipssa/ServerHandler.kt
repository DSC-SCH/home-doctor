package com.khnsoft.zipssa

import com.google.gson.JsonObject

class ServerHandler {
    companion object {
        fun isUserExists(type: AccountType, uid: String) : Boolean {
            // TODO("Check whether the user is registered in server")
            val jAuth = JsonObject()
            jAuth.addProperty("type", type.type)
            jAuth.addProperty("uid", uid)

            return true
        }

        fun send(remote: EndOfAPI, method: HttpMethod, msg: String?) : String{
            val sMsg : String?
            if (method == HttpMethod.POST || method == HttpMethod.PUT) {
                if (msg == null)
                    return HttpAttr.ERROR_MSG
                else
                    sMsg = msg
            } else if (method == HttpMethod.GET || method == HttpMethod.DELETE) {
                sMsg = null
            } else {
                return HttpAttr.ERROR_MSG
            }

            return HttpAsyncTask().execute(remote.remote, method.method, sMsg).get()
        }
    }
}