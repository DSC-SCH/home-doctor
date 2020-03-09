package com.khnsoft.zipssa

import com.google.gson.JsonObject

class ServerHandler {
    companion object {
        fun send(api: EndOfAPI, msg: String? = null, id: Int? = null) : String{
            val method = api.method
            val sMsg : String?
            sMsg = if (method == HttpMethod.POST || method == HttpMethod.PUT) {
                msg ?: return HttpAttr.ERROR_MSG
            } else if (method == HttpMethod.GET || method == HttpMethod.DELETE) {
                null
            } else {
                return HttpAttr.ERROR_MSG
            }

            val remote : String = if (api.isIdNeeded) {
                if (id == null)
                    return HttpAttr.ERROR_MSG
                else
                    "${api.remote}/${id}"
            } else {
                api.remote
            }

            return HttpAsyncTask().execute(remote, method.method, sMsg).get()
        }

        fun isUserExists(type: AccountType, uid: String) : Boolean {
            // TODO("Check whether the user is registered in server")
            val jAuth = JsonObject()
            jAuth.addProperty("type", type.type)
            jAuth.addProperty("uid", uid)

            return true
        }
    }
}