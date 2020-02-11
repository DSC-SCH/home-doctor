package com.dscsch.zipssa

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.lang.Exception

class MyDbOpenHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
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
			|START_DT CHAR(10) NOT NULL,
			|END_DT CHAR(10) NOT NULL,
			|TIMES VARCHAR(128),
			|REPEATS VARCHAR(32),
			|ALARM INTEGER(1) NOT NULL,
			|LABEL INTEGER
			|)
		""".trimMargin()
		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}