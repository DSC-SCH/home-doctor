package com.khnsoft.zipssa

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.Exception

class MyDbOpenHelper(val context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
	SQLiteOpenHelper(context, name, factory, version) {

	override fun onCreate(db: SQLiteDatabase) {
		createTableUser(db)
		createTableLabel(db)
		createTableAlarm(db)
		createTableImage(db)
		createTableTaken(db)
	}

	override fun onOpen(db: SQLiteDatabase?) {
		db?.setForeignKeyConstraintsEnabled(true)
	}

	override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

	}

	fun createTableUser(db: SQLiteDatabase) {
		val sql = """
			CREATE TABLE IF NOT EXISTS USER_TB (
			user_id INTEGER PRIMARY KEY AUTOINCREMENT,
			user_name VARCHAR(20) NOT NULL,
			created_date CHAR(10) NOT NULL,
			last_modified_date CHAR(10) NOT NULL
			)
		""".trimIndent()

		MyLogger.i("Initialize USER_TB", sql)

		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun createTableLabel(db: SQLiteDatabase) {
		val sql = """
			CREATE TABLE IF NOT EXISTS LABEL_TB (
			label_id INTEGER PRIMARY KEY AUTOINCREMENT,
			label_user INTEGER NOT NULL REFERENCES USER_TB(user_id) ON DELETE CASCADE,
			label_title VARCHAR(10) NOT NULL,
			label_color CHAR(7) NOT NULL,
			created_date CHAR(10) NOT NULL,
			last_modified_date CHAR(10) NOT NULL
			)
		""".trimIndent()

		MyLogger.i("Initialize LABEL_TB", sql)

		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun createTableAlarm(db: SQLiteDatabase) {
		val sql = """
			CREATE TABLE IF NOT EXISTS ALARM_TB (
			alarm_id INTEGER PRIMARY KEY AUTOINCREMENT,
			alarm_title VARCHAR(64) NOT NULL,
			alarm_user INTEGER NOT NULL REFERENCES USER_TB(user_id) ON DELETE CASCADE,
			alarm_label INTEGER NOT NULL REFERENCES LABEL_TB(label_id) ON DELETE CASCADE,
			alarm_start_date CHAR(10) NOT NULL,
			alarm_end_date CHAR(10) NOT NULL,
			alarm_times VARCHAR(128) NOT NULL,
			alarm_repeats VARCHAR(32) NOT NULL,
			alarm_enabled VARCHAR(32) NOT NULL,
			created_date CHAR(10) NOT NULL,
			last_modified_date CHAR(10) NOT NULL
			)
		""".trimIndent()

		MyLogger.i("Initialize ALARM_TB", sql)

		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun createTableImage(db: SQLiteDatabase) {
		val sql = """
			CREATE TABLE IF NOT EXISTS IMAGE_TB (
			image_id INTEGER PRIMARY KEY AUTOINCREMENT,
			alarm_id INTEGER NOT NULL REFERENCES ALARM_TB(alarm_id) ON DELETE CASCADE,
			image TEXT NOT NULL,
			created_date CHAR(10) NOT NULL,
			last_modified_date CHAR(10) NOT NULL
			)
		""".trimIndent()

		MyLogger.i("Initialize IMAGE_TB", sql)

		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun createTableTaken(db: SQLiteDatabase) {
		val sql = """
			CREATE TABLE IF NOT EXISTS TAKEN_TB (
			taken_alarm_id INTEGER REFERENCES ALARM_TB(alarm_id) ON DELETE CASCADE,
			taken_date CHAR(10),
			taken_count INTEGER,
			send_failed INTEGER,
			PRIMARY KEY (taken_alarm_id, taken_date)
			)
		""".trimIndent()

		MyLogger.i("Initialize TAKEN_TB", sql)

		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}