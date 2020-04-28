package com.designvalue.medihand_en

import android.content.Context
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class ServerHandler {
    companion object {
        val alarmToServer = mapOf(
            "alarm_id" to "alarmId",
            "alarm_title" to "title",
            "alarm_user" to "user",
            "alarm_label" to "label",
            "alarm_start_date" to "startDate",
            "alarm_end_date" to "endDate",
            "alarm_times" to "times",
            "alarm_repeats" to "repeats",
            "alarm_enabled" to "alarmStatus",
            "label_title" to "labelTitle",
            "label_color" to "color",
            "created_date" to "createdDate",
            "last_modified_date" to "lastModifiedDate"
        )

        val labelToServer = mapOf(
            "label_id" to "labelId",
            "label_user" to "user",
            "label_title" to "title",
            "label_color" to "color"
        )

        val imageToServer = mapOf(
            "image_id" to "imageId",
            "alarm_id" to "alarm",
            "alarm_title" to "alarmTitle",
            "label_color" to "labelColor",
            "created_date" to "createdDate"
        )

        val alarmToLocal : Map<String, String>
        val labelToLocal : Map<String, String>
        val imageToLocal : Map<String, String>

        init {
            val alarm = mutableMapOf<String, String>()
            for (entry in alarmToServer) {
                alarm[entry.value] = entry.key
            }
            alarmToLocal = alarm.toMap()

            val label = mutableMapOf<String, String>()
            for (entry in labelToServer) {
                label[entry.value] = entry.key
            }
            labelToLocal = label.toMap()

            val image = mutableMapOf<String, String>()
            for (entry in imageToServer) {
                image[entry.value] = entry.key
            }
            imageToLocal = image.toMap()
        }

        fun convertKeys(jOrigin: JsonObject, keyMap: Map<String, String>) : JsonObject {
            val jRet = JsonObject()

            for (key in jOrigin.keySet()) {
                if (keyMap.containsKey(key)) {
                    jRet.addProperty(keyMap[key], jOrigin[key].asString)
                } else {
                    jRet.addProperty(key, jOrigin[key].asString)
                }
            }

            return jRet
        }

        fun send(context: Context? = null, api: EndOfAPI, data: JsonObject? = null, id: Int? = null, id2: Int? = null): JsonObject {
            val method = api.method
            val json: JsonObject?
            json = if (method == HttpMethod.POST || method == HttpMethod.PUT) {
                if (data == null) return HttpHelper.getError(HttpError.METHOD_NO_DATA)
                when(api) {
                    EndOfAPI.ADD_ALARM, EndOfAPI.EDIT_ALARM, EndOfAPI.CHANGE_ALARM_STATE,
                    EndOfAPI.SYNC_ADD_ALARM, EndOfAPI.SYNC_EDIT_ALARM -> convertKeys(data, alarmToServer)
                    EndOfAPI.ADD_LABEL, EndOfAPI.EDIT_LABEL, EndOfAPI.SYNC_ADD_LABEL -> convertKeys(data, labelToServer)
                    else -> data
                }
            } else if (method == HttpMethod.GET || method == HttpMethod.DELETE) {
                null
            } else {
                return HttpHelper.getError(HttpError.METHOD_ERROR)
            }

            val remote: String = if (api.isIdNeeded) {
                if (id == null) // Need id but none given
                    return HttpHelper.getError(HttpError.NO_ID1)
                else if (api.isId2Needed) {
                    if (id2 == null) // Need id2 but none given
                        return HttpHelper.getError(HttpError.NO_ID2)
                    else // Need id1 and id2 and all given
                        "${api.remote}/${id}/${id2}"
                } else { // Need id1 and given
                    "${api.remote}/${id}"
                }
            } else {
                api.remote
            }

            if (context != null && UserData.token == null) {
                val sp = SPHandler.getSp(context)
                UserData.token = sp.getString(AlarmReceiver.SP_TOKEN, "")
            }

            when (api) {
                EndOfAPI.GET_NOTICES, EndOfAPI.GET_CONTRACTS, EndOfAPI.ENQUIRE, EndOfAPI.USER_LOGIN,
                EndOfAPI.SEARCH_NAME, EndOfAPI.SEARCH_TOTAL, EndOfAPI.CHECK_INTERNET -> {
                    return getBody(JsonParser.parseString(HttpAsyncTask().execute(remote, method.name, json.toString()).get()).asJsonObject)
                }
                EndOfAPI.LOCAL_USER_REGISTER, EndOfAPI.LOCAL_USER_SERVER, EndOfAPI.LOCAL_LABEL_SERVER, EndOfAPI.LOCAL_ALARM_SERVER -> {
                    if (context == null)
                        return HttpHelper.getError(HttpError.NO_CONTEXT)
                    return DatabaseHandler.execOfflineAPI(context, api, data, id)
                }
                else -> {
                    if (UserData.isOffline()) {
                        if (context == null)
                            return HttpHelper.getError(HttpError.NO_CONTEXT)
                        return DatabaseHandler.execOfflineAPI(context, api, data, id)
                    }
                }
            }

            return getBody(JsonParser.parseString(HttpAsyncTask().execute(remote, method.name, json.toString()).get()).asJsonObject)
        }

        fun getBody(jRet: JsonObject) : JsonObject {
            return if (!jRet.has("body")) jRet
            else jRet["body"].asJsonObject
        }
    }
}