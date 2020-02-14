package com.khnsoft.zipssa

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

class MyDbOpenHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
	SQLiteOpenHelper(context, name, factory, version) {

	override fun onCreate(db: SQLiteDatabase) {
		createTableAlarms(db)
		createTableLabels(db)
		insertDefaultLabels(db)
	}

	override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

	}

	fun createTableAlarms(db: SQLiteDatabase) {
		val sql = """
			|CREATE TABLE IF NOT EXISTS ALARM_TB (
			|_ID INTEGER PRIMARY KEY AUTOINCREMENT,
			|ALARM_TITLE VARCAHR(64) NOT NULL,
			|ALARM_START_DT CHAR(10) NOT NULL,
			|ALARM_END_DT CHAR(10) NOT NULL,
			|ALARM_TIMES VARCHAR(128),
			|ALARM_REPEATS VARCHAR(32),
			|ALARM_ENABLED INTEGER(1) NOT NULL,
			|ALARM_LABEL INTEGER
			|)
		""".trimMargin()

		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun createTableLabels(db: SQLiteDatabase) {
		val sql = """
			|CREATE TABLE IF NOT EXISTS LABEL_TB (
			|_ID INTEGER PRIMARY KEY AUTOINCREMENT,
			|LABEL_TITLE VARCHAR(16) NOT NULL,
			|LABEL_COLOR CHAR(7) NOT NULL
			|)
		""".trimMargin()

		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun insertDefaultLabels(db: SQLiteDatabase) {
		try {
			var sql = """
			|INSERT INTO LABEL_TB (_ID, LABEL_TITLE, LABEL_COLOR) 
			|VALUES (1, "없음","#FFFFFF")
			""".trimMargin()
			db.execSQL(sql)

			sql = """
			|INSERT INTO LABEL_TB (_ID, LABEL_TITLE, LABEL_COLOR) 
			|VALUES (2, "감기","#FFD5D5")
			""".trimMargin()
			db.execSQL(sql)

			sql = """
			|INSERT INTO LABEL_TB (_ID, LABEL_TITLE, LABEL_COLOR) 
			|VALUES (3, "비염","#B6F6B2")
			""".trimMargin()
			db.execSQL(sql)

			sql = """
			|INSERT INTO LABEL_TB (_ID, LABEL_TITLE, LABEL_COLOR) 
			|VALUES (4, "알레르기","#D6D6FF")
			""".trimMargin()
			db.execSQL(sql)
		} catch (e: Exception) {
		}
	}
}