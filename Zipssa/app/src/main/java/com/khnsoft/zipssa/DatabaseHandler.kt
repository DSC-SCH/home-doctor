package com.khnsoft.zipssa

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.lang.Exception
import java.util.*

class DatabaseHandler(context: Context?) {
	lateinit var mDB: SQLiteDatabase
	val mHelper: MyDbOpenHelper

	init {
		mHelper = MyDbOpenHelper(context, DB_NAME, null, DB_VERSION)
	}

	companion object {
		const val DB_NAME = "MediHand.db"
		const val DB_VERSION = 1


		fun open(context: Context?): DatabaseHandler {
			return DatabaseHandler(context)
		}

		fun initLabels(mHandler: DatabaseHandler, id: Int? = null) {
			try {
				var sql = """
                    SELECT label_id FROM LABEL_TB WHERE label_user=${UserData.id}
                """.trimIndent()

				val lLabel = mHandler.execResult(sql)

				if (lLabel.size() == 0) {
					sql = """
                        INSERT INTO LABEL_TB (label_id, label_user, label_title, label_color, created_date, last_modified_date) 
                        VALUES (1, ${UserData.id}, "없음", "#FFFFFF", "2020-03-10", "2020-03-10")
                    """.trimIndent()
					mHandler.execNonResult(sql)

					sql = """
                        INSERT INTO LABEL_TB (label_id, label_user, label_title, label_color, created_date, last_modified_date) 
                        VALUES (2, ${UserData.id}, "감기", "#FFD5D5", "2020-03-10", "2020-03-10")
                    """.trimIndent()
					mHandler.execNonResult(sql)

					sql = """
                        INSERT INTO LABEL_TB (label_id, label_user, label_title, label_color, created_date, last_modified_date) 
                        VALUES (3, ${UserData.id}, "비염", "#B6F6B2", "2020-03-10", "2020-03-10")
                    """.trimIndent()
					mHandler.execNonResult(sql)

					sql = """
                        INSERT INTO LABEL_TB (label_id, label_user, label_title, label_color, created_date, last_modified_date) 
                        VALUES (4, ${UserData.id}, "알레르기", "#D6D6FF", "2020-03-10", "2020-03-10")
                    """.trimIndent()
					mHandler.execNonResult(sql)
				}
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}

		fun execOfflineAPI(context: Context, api: EndOfAPI, data: JsonObject? = null, id: Int? = null): JsonObject {
			val mHandler = open(context)
			val curDate = SDF.dateBar.format(Calendar.getInstance().time)

			try {
				when (api) {
					EndOfAPI.OFFLINE_USER_REGISTER -> {
						val jItem = data!!
						val sql = """
                            INSERT INTO USER_TB (user_id, user_name, created_date, last_modified_date) 
                            VALUES (
                            ${jItem["user_id"].asInt},
                            '${jItem["user_name"].asString}',
                            '${curDate}',
                            '${curDate}'
                            )
                        """.trimIndent()

						mHandler.execNonResult(sql)
						return HttpHelper.getOK()
					}
					EndOfAPI.GET_ENABLED_ALARMS -> {
						val sql = """
							SELECT alarm_id, alarm_title, alarm_user, alarm_label, alarm_start_date, alarm_end_date, alarm_times,
							alarm_repeats, alarm_enabled, ALARM_TB.created_date, ALARM_TB.last_modified_date, label_title, label_color 
							FROM ALARM_TB 
							LEFT OUTER JOIN LABEL_TB ON ALARM_TB.alarm_label=LABEL_TB.label_id 
							WHERE alarm_user=${UserData.id} AND alarm_enabled='${AlarmStatus.ENABLED.status}'
						""".trimIndent()

						val lAlarms = mHandler.execResult(sql)
						return HttpHelper.getOK(lAlarms)
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
                            '${curDate}',
                            '${curDate}'
                            )
                        """.trimIndent()

						mHandler.execNonResult(sql)
						return HttpHelper.getOK()
					}
					EndOfAPI.GET_LABELS -> {
						initLabels(mHandler)

						val sql = """
                            SELECT label_id, label_title, label_color, created_date, last_modified_date 
                            FROM LABEL_TB WHERE label_user=${UserData.id}
                        """.trimIndent()

						val lLabels = mHandler.execResult(sql)
						return HttpHelper.getOK(lLabels)
					}
					EndOfAPI.EDIT_LABEL -> {
						val jItem = data!!

						val sql = """
                            UPDATE LABEL_TB SET 
                            label_title='${jItem["label_title"].asString}',
                            label_color='${jItem["label_color"].asString}',
                            last_modified_date='${curDate}' 
                            WHERE label_id=${id}
                        """.trimIndent()

						mHandler.execNonResult(sql)
						return HttpHelper.getOK()
					}
					EndOfAPI.DELETE_LABEL -> {
						val sql = """
                            DELETE FROM LABEL_TB WHERE label_id=${id}
                        """.trimIndent()

						mHandler.execNonResult(sql)
						return HttpHelper.getOK()
					}
					EndOfAPI.GET_ALARM -> {
						val sql = """
                            SELECT alarm_id, alarm_title, alarm_user, alarm_label, alarm_start_date, alarm_end_date, alarm_times,
                            alarm_repeats, alarm_enabled, ALARM_TB.created_date, ALARM_TB.last_modified_date, label_title, label_color 
                            FROM ALARM_TB 
                            LEFT OUTER JOIN LABEL_TB ON ALARM_TB.alarm_label=LABEL_TB.label_id 
                            WHERE alarm_id=${id}
                        """.trimIndent()

						val lAlarms = mHandler.execResult(sql)
						if (lAlarms.size() > 0) {
							return HttpHelper.getOK(lAlarms[0].asJsonObject)
						}
						return HttpHelper.getError()
					}
					EndOfAPI.GET_ALL_ALARMS -> {
						val sql = """
                            SELECT alarm_id, alarm_title, alarm_user, alarm_label, alarm_start_date, alarm_end_date, alarm_times,
                            alarm_repeats, alarm_enabled, ALARM_TB.created_date, ALARM_TB.last_modified_date, label_title, label_color 
                            FROM ALARM_TB 
                            LEFT OUTER JOIN LABEL_TB ON ALARM_TB.alarm_label=LABEL_TB.label_id 
                            WHERE alarm_user=${UserData.id}
                        """.trimIndent()

						val lAlarms = mHandler.execResult(sql)
						return HttpHelper.getOK(lAlarms)
					}
					EndOfAPI.ADD_ALARM -> {
						val jItem = data!!
						var sql = """
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
                            '${curDate}',
                            '${curDate}'
                            )
                        """.trimIndent()

						mHandler.execNonResult(sql)

						sql = """
                            SELECT alarm_id, alarm_start_date, alarm_end_date FROM ALARM_TB ORDER BY alarm_id DESC limit 1
                        """.trimIndent()

						val result = mHandler.execResult(sql)
						if (result.size() > 0) {
							val jResult = result[0].asJsonObject
							val curCal = Calendar.getInstance()
							curCal.time = SDF.dateBar.parse(jResult["alarm_start_date"].asString)
							val endTime = SDF.dateBar.parse(jResult["alarm_end_date"].asString).time
							while (curCal.timeInMillis <= endTime) {
								if (jItem["alarm_repeats"].asString.contains(curCal[Calendar.DAY_OF_WEEK].toString())) {
									sql = """
										INSERT INTO TAKEN_TB (taken_alarm_id, taken_date, taken_count)
										VALUES (
										${jResult["alarm_id"].asInt},
										'${SDF.dateBar.format(curCal.time)}',
										0
										)
									""".trimIndent()

									mHandler.execNonResult(sql)
								}
								curCal.add(Calendar.DAY_OF_MONTH, 1)
							}

							val jRet = JsonObject()
                            jRet.addProperty("id", jResult["alarm_id"].asInt)
							return HttpHelper.getOK(jRet)
						}

						return HttpHelper.getError()
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
                            last_modified_date='${curDate}'
                            WHERE alarm_id=${id}
                        """.trimIndent()

						mHandler.execNonResult(sql)
						return HttpHelper.getOK()
					}
					EndOfAPI.CHANGE_ALARM_STATE -> {
						val jItem = data!!

						val sql = """
                            UPDATE ALARM_TB SET 
                            alarm_enabled='${jItem["alarm_enabled"].asString}' 
                            WHERE alarm_id=${id}
                        """.trimIndent()

						mHandler.execNonResult(sql)
						return HttpHelper.getOK()
					}
					EndOfAPI.DELETE_ALARM -> {
						val sql = """
                            DELETE FROM ALARM_TB WHERE alarm_id=${id}
                        """.trimIndent()

						mHandler.execNonResult(sql)
						return HttpHelper.getOK()
					}
					EndOfAPI.ADD_IMAGE -> {
						val jItem = data!!
						val images = jItem["image"].asJsonArray

						for (image in images) {
							val sql = """
                                INSERT INTO IMAGE_TB (alarm_id, image, created_date, last_modified_date) 
                                VALUES (
                                ${id},
                                '${image.asString}',
                                '${curDate}',
                                '${curDate}'
                                )
                            """.trimIndent()

							mHandler.execNonResult(sql)
						}

						return HttpHelper.getOK()
					}
					EndOfAPI.EDIT_IMAGES -> {
						val jItem = data!!
						val images = jItem["image"].asJsonArray

						var sql = """
                            DELETE FROM IMAGE_TB WHERE alarm_id=${id}
                        """.trimIndent()

						mHandler.execNonResult(sql)

						for (image in images) {
							sql = """
                                INSERT INTO IMAGE_TB (alarm_id, image, created_date, last_modified_date) 
                                VALUES (
                                ${id},
                                '${image.asString}',
                                '${curDate}',
                                '${curDate}'
                                )
                            """.trimIndent()

							mHandler.execNonResult(sql)
						}

						return HttpHelper.getOK()
					}
					EndOfAPI.GET_IMAGES -> {
						val sql = """
                            SELECT image_id, IMAGE_TB.alarm_id, image FROM IMAGE_TB 
                            LEFT OUTER JOIN ALARM_TB ON IMAGE_TB.alarm_id=ALARM_TB.alarm_id
                            WHERE alarm_user=${UserData.id} AND IMAGE_TB.alarm_id=${id}
                        """.trimIndent()

						val lImages = mHandler.execResult(sql)

						return HttpHelper.getOK(lImages)
					}
					EndOfAPI.GET_ALL_IMAGES -> {
						val sql = """
                            SELECT image_id, IMAGE_TB.alarm_id, alarm_title, image, label_color FROM IMAGE_TB 
                            LEFT OUTER JOIN ALARM_TB ON IMAGE_TB.alarm_id=ALARM_TB.alarm_id  
                            LEFT OUTER JOIN LABEL_TB ON ALARM_TB.alarm_label=LABEL_TB.label_id 
                            WHERE alarm_user=${UserData.id}
                        """.trimIndent()

						val lImages = mHandler.execResult(sql)

						return HttpHelper.getOK(lImages)
					}
					else -> {
						return HttpHelper.getError()
					}
				}
			} catch (e: Exception) {
				e.printStackTrace()
				return HttpHelper.getError()
			} finally {
				mHandler.close()
			}
		}
	}

	fun execNonResult(sql: String): Boolean {
		mDB = mHelper.writableDatabase

		MyLogger.i("Execute SQL for None", sql)
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
		MyLogger.i("Execute SQL for Result", sql)
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