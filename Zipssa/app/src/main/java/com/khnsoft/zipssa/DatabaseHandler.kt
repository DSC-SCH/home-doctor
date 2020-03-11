package com.khnsoft.zipssa

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.lang.Exception

class DatabaseHandler(context: Context?) {
    lateinit var mDB: SQLiteDatabase
    val mHelper: MyDbOpenHelper

    init {
        mHelper = MyDbOpenHelper(context, DB_NAME, null, DB_VERSION)
    }

    companion object {
        const val DB_NAME = "Zipssa.db"
        const val DB_VERSION = 1

        fun open(context: Context?): DatabaseHandler {
            return DatabaseHandler(context)
        }

        fun update(context: Context?): Boolean {
            if (UserData.accountType == AccountType.OFFLINE)
                return true

            val mHandler = open(context)
            val result = mHandler.updateTables(UserData.id)
            mHandler.close()

            return result
        }

        fun initLabels(mHandler: DatabaseHandler, id: Int? = null) {

            try {
                var sql = """
                    SELECT label_id FROM LABEL_TB WHERE label_user=${UserData.id}
                """.trimIndent()

                val lLabel = mHandler.execResult(sql)

                if (lLabel.size() == 0) {
                    sql = """
                        |INSERT INTO LABEL_TB (label_id, label_user, label_title, label_color, created_date, last_modified_date) 
                        |VALUES (1, ${UserData.id}, "없음", "#FFFFFF", "2020-03-10", "2020-03-10")
                    """.trimMargin()
                    mHandler.execNonResult(sql)

                    sql = """
                        |INSERT INTO LABEL_TB (label_id, label_user, label_title, label_color, created_date, last_modified_date) 
                        |VALUES (2, ${UserData.id}, "감기", "#FFD5D5", "2020-03-10", "2020-03-10")
                    """.trimMargin()
                    mHandler.execNonResult(sql)

                    sql = """
                        |INSERT INTO LABEL_TB (label_id, label_user, label_title, label_color, created_date, last_modified_date) 
                        |VALUES (3, ${UserData.id}, "비염", "#B6F6B2", "2020-03-10", "2020-03-10")
                    """.trimMargin()
                    mHandler.execNonResult(sql)

                    sql = """
                        |INSERT INTO LABEL_TB (label_id, label_user, label_title, label_color, created_date, last_modified_date) 
                        |VALUES (4, ${UserData.id}, "알레르기", "#D6D6FF", "2020-03-10", "2020-03-10")
                    """.trimMargin()
                    mHandler.execNonResult(sql)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun execOfflineAPI(context: Context, api: EndOfAPI, data: JsonObject? = null, id: Int? = null): JsonObject {
            val mHandler = open(context)

            try {
                when (api) {
                    EndOfAPI.USER_REGISTER -> {
                        val jItem = data!!
                        val sql = """
                            INSERT INTO USER_TB (user_id, user_name, created_date, last_modified_date) 
                            VALUES (
                            ${jItem["user_id"].asInt},
                            '${jItem["user_name"].asString}',
                            '${jItem["created_date"].asString}',
                            '${jItem["last_modified_date"].asString}'
                            )
                        """.trimIndent()

                        mHandler.execNonResult(sql)
                        return HttpAttr.OK_MSG
                    }
                    EndOfAPI.GET_ENABLED_ALARMS -> {
                        val sql = """
							|SELECT alarm_id, alarm_title, alarm_user, alarm_label, alarm_start_date, alarm_end_date, alarm_times,
							|alarm_repeats, alarm_enabled, ALARM_TB.created_date, ALARM_TB.last_modified_date, label_title, label_color 
							|FROM ALARM_TB 
							|LEFT OUTER JOIN LABEL_TB ON ALARM_TB.alarm_label=LABEL_TB.label_id 
							|WHERE alarm_user=${UserData.id} AND alarm_enabled='${AlarmStatus.ENABLED.status}'
						""".trimMargin()

                        val lAlarms = mHandler.execResult(sql)
                        val ret = JsonObject()
                        ret.add("array", lAlarms)
                        return ret
                    }
                    EndOfAPI.ADD_LABEL -> {
                        initLabels(mHandler)

                        val jItem = data!!
                        val sql = """
                            INSERT INTO LABEL_TB (label_user, label_title, label_color, 
                            created_date, last_modified_date) 
                            VALUES (
                            ${jItem["label_user"].asInt},
                            '${jItem["label_title"].asString}',
                            '${jItem["label_color"].asString}',
                            '${jItem["created_date"].asString}',
                            '${jItem["last_modified_date"].asString}'
                            )
                        """.trimIndent()

                        mHandler.execNonResult(sql)
                        return HttpAttr.OK_MSG
                    }
                    EndOfAPI.GET_LABELS -> {
                        initLabels(mHandler)

                        val sql = """
                            |SELECT label_id, label_title, label_color, created_date, last_modified_date 
                            |FROM LABEL_TB WHERE label_user=${UserData.id}
                        """.trimMargin()

                        val lLabels = mHandler.execResult(sql)
                        val ret = JsonObject()
                        ret.add("array", lLabels)
                        return ret
                    }
                    EndOfAPI.ADD_ALARM -> {
                        val jItem = data!!
                        val sql = """
                            INSERT INTO ALARM_TB (alarm_title, alarm_user, alarm_label, alarm_start_date, alarm_end_date,  
                            alarm_times, alarm_repeats, alarm_enabled, created_date, last_modified_date) 
                            VALUES (
                            '${jItem["alarm_title"].asString}',
                            ${jItem["alarm_user"].asInt},
                            ${jItem["alarm_label"].asInt},
                            '${jItem["alarm_start_date"].asString}',
                            '${jItem["alarm_end_date"].asString}',
                            '${jItem["alarm_times"].asString}',
                            '${jItem["alarm_repeats"].asString}',
                            '${jItem["alarm_enabled"].asString}',
                            '${jItem["created_date"].asString}',
                            '${jItem["last_modified_date"].asString}'
                            )
                        """.trimIndent()

                        mHandler.execNonResult(sql)
                        return HttpAttr.OK_MSG
                    }
                    EndOfAPI.EDIT_ALARM -> {
                        val jItem = data!!
                        val sql = """
                            UPDATE ALARM_TB SET 
                            alarm_title='${jItem["alarm_title"].asString}',
                            alarm_label=${jItem["alarm_label"].asInt},
                            alarm_start_date='${jItem["alarm_start_date"].asString}',
                            alarm_end_date='${jItem["alarm_end_date"].asString}',
                            alarm_times='${jItem["alarm_times"].asString}',
                            alarm_repeats='${jItem["alarm_repeats"].asString}',
                            alarm_enabled='${jItem["alarm_enabled"].asString}',
                            last_modified_date='${jItem["last_modified_date"].asString}'
                            WHERE alarm_id=${id}
                        """.trimIndent()

                        mHandler.execNonResult(sql)
                        return HttpAttr.OK_MSG
                    }
                    EndOfAPI.DELETE_ALARM -> {
                        val sql = """
                            DELETE FROM ALARM_TB WHERE alarm_id=${id}
                        """.trimIndent()

                        mHandler.execNonResult(sql)
                        return HttpAttr.OK_MSG
                    }
                    else -> {
                        return HttpAttr.ERROR_MSG
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return HttpAttr.ERROR_MSG
            } finally {
                mHandler.close()
            }
        }
    }

    fun updateTables(userId: Int): Boolean {
        // TODO("Get CHECK_TB from server")
        val jServer = JsonObject()

        mDB = mHelper.writableDatabase

        val sql = "SELECT * FROM CHECK_TB WHERE user_id=${userId}"
        val lRet = execResult(sql)
        if (lRet.size() > 0) {
            val jLocal = lRet[0] as JsonObject
            for (tb in jLocal.keySet()) {
                if (tb == "user_id") continue
                if (jServer[tb].asInt != jLocal[tb].asInt) {
                    // TODO("Update table")
                }
            }
            return true
        }
        return false
    }

    fun execNonResult(sql: String): Boolean {
        mDB = mHelper.writableDatabase

        Log.i("Execute SQL for None", sql)
        try {
            mDB.execSQL(sql)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun execResult(sql: String): JsonArray {
        mDB = mHelper.readableDatabase

        val jRet = JsonArray()
        Log.i("Execute SQL for Result", sql)
        try {
            val cursor = mDB.rawQuery(sql, null)
            val colNames = cursor.columnNames
            val colCounts = cursor.columnCount
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val jItem = JsonObject()
                    for (i in 0..colCounts - 1) {
                        jItem.addProperty(colNames[i], cursor.getString(i))
                    }
                    jRet.add(jItem)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jRet
    }

    fun close() {
        mHelper.close()
    }
}