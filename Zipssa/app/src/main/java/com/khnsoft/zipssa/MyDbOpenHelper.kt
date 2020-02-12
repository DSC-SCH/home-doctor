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

	fun createTableLabels(db: SQLiteDatabase) {
		val sql = """
			|CREATE TABLE IF NOT EXISTS LABELS (
			|_ID INTEGER PRIMARY KEY AUTOINCREMENT,
			|TITLE VARCHAR(16) NOT NULL,
			|COLOR CHAR(6) NOT NULL
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
			|INSERT INTO LABELS (_ID, TITLE, COLOR) 
			|VALUES (1, "감기","FFD5D5")
			""".trimMargin()
			db.execSQL(sql)

			sql = """
			|INSERT INTO LABELS (_ID, TITLE, COLOR) 
			|VALUES (2, "비염","B6F6B2")
			""".trimMargin()
			db.execSQL(sql)

			sql = """
			|INSERT INTO LABELS (_ID, TITLE, COLOR) 
			|VALUES (3, "알레르기","D6D6FF")
			""".trimMargin()
			db.execSQL(sql)
		} catch (e: Exception) {
		}
	}
}