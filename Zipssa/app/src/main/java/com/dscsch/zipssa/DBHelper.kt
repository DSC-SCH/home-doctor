package com.dscsch.zipssa

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.lang.Exception

class DBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
	SQLiteOpenHelper(context, name, factory, version) {

	override fun onCreate(db: SQLiteDatabase) {
		createTableAlarms(db)
	}

	override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

	}

	fun createTableAlarms(db: SQLiteDatabase) {
		val sql = """
			|CREATE TABLE IF NOT EXISTS ALARMS (
			|_ID INTEGER PRIMARY KEY AUTOINCREMENT,
			|TITLE VARCAHR(64) NOT NULL,
			|START_DT DATE NOT NULL,
			|END_DT DATE NOT NULL,
			|TIMES VARCHAR(128),
			|REPEATS VARCHAR(32),
			|ALARM INTEGER(1) NOT NULL,
			|LABEL INTEGER
			|)
		""".trimMargin()
		Log.i("@@@", "Create table ALARMS")
		Log.i("@@@", sql)
		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun insertAlarm(db: SQLiteDatabase, sql: String) {
		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun getAlarm(db: SQLiteDatabase, sql: String) : JsonArray {
		val jRet = JsonArray()
		try {
			val cursor = db.rawQuery(sql, null)
			val colNames = cursor.columnNames
			val colCounts = cursor.columnCount
			if (cursor.count > 0) {
				while (cursor.moveToNext()) {
					val jItem = JsonObject()
					for (i in 0..colCounts-1) {
						jItem.addProperty(colNames[i], cursor.getString(i))
					}
					jRet.add(jItem)
				}
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
		return jRet
	}
}