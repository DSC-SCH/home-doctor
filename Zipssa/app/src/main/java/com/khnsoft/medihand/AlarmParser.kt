package com.khnsoft.medihand

import com.google.gson.JsonArray
import com.google.gson.JsonParser

class AlarmParser {
    companion object {
        fun parseTimes(string: String) : JsonArray {
            val ret = string.split("/").joinToString { it -> "\"${it}\"" }
            return JsonParser.parseString("[${if (ret.equals("\"\"")) "" else ret}]").asJsonArray
        }

        fun parseRepeats(string: String) : JsonArray {
            return JsonParser.parseString("[${string.split("/").joinToString()}]").asJsonArray
        }

        fun parseStatus(string: String) : AlarmStatus {
            for (value in AlarmStatus.values()) {
                if (value.name.equals(string)) return value
            }
            return AlarmStatus.valueOf(string)
        }
    }
}