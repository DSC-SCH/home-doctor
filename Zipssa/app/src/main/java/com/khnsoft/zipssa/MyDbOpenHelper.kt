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
		createTableCare(db)
		createTableCheck(db)
	}

	override fun onOpen(db: SQLiteDatabase?) {
		db?.setForeignKeyConstraintsEnabled(true)
	}

	override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

	}

	fun createTableUser(db: SQLiteDatabase) {
		val sql = """
			|CREATE TABLE IF NOT EXISTS USER_TB (
			|user_id INTEGER PRIMARY KEY AUTOINCREMENT,
			|user_name VARCHAR(20) NOT NULL,
			|created_date CHAR(10) NOT NULL,
			|last_modified_date CHAR(10) NOT NULL
			|)
		""".trimMargin()

		Log.i("Initialize USER_TB", sql)

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

		Log.i("Initialize LABEL_TB", sql)

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

		Log.i("Initialize ALARM_TB", sql)

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
			image LONGBLOB NOT NULL,
			created_date CHAR(10) NOT NULL,
			last_modified_date CHAR(10) NOT NULL
			)
		""".trimIndent()

		Log.i("Initialize IMAGE_TB", sql)

		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun createTableCare(db: SQLiteDatabase) {
		val sql = """
			CREATE TABLE IF NOT EXISTS CARE_TB (
			connection_id INTEGER PRIMARY KEY AUTOINCREMENT,
			user_id INTEGER NOT NULL REFERENCES USER_TB(user_id) ON DELETE CASCADE,
			care_user INTEGER NOT NULL REFERENCES USER_TB(user_id) ON DELETE CASCADE,
			created_date CHAR(10) NOT NULL,
			last_modified_date CHAR(10) NOT NULL	
			)
		""".trimIndent()

		Log.i("Initialize CARE_TB", sql)

		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun createTableCheck(db: SQLiteDatabase) {
		val sql = """
			CREATE TABLE IF NOT EXISTS CHECK_TB (
			user_id INTEGER PRIMARY KEY REFERENCES USER_TB(user_id) ON DELETE CASCADE,
			user INTEGER NOT NULL DEFAULT 0,
			label INTEGER NOT NULL DEFAULT 0,
			alarm INTEGER NOT NULL DEFAULT 0,
			image INTEGER NOT NULL DEFAULT 0,
			care INTEGER NOT NULL DEFAULT 0
			)
		""".trimIndent()

		Log.i("Initialize CHECK_TB", sql)

		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}