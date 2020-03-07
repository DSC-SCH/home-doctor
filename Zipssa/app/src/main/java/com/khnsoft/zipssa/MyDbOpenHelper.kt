package com.khnsoft.zipssa

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

class MyDbOpenHelper(val context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
	SQLiteOpenHelper(context, name, factory, version) {

	companion object {
		const val SP_UID = "user_id"
		const val UID_DEFAULT = -1
	}

	override fun onCreate(db: SQLiteDatabase) {
		val sp = context?.getSharedPreferences(SharedPreferencesSrc.SP_DATABASE_NAME, Context.MODE_PRIVATE)
		val user_id = sp?.getInt(SP_UID, UID_DEFAULT) ?: -1

		createTableUser(db)
		createTableLabel(db)
		createTableAlarm(db)
		createTableImage(db)
		createTableCare(db)

		insertDefaultLabels(db)
	}

	override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

	}

	fun createTableUser(db: SQLiteDatabase) {
		val sql = """
			|CREATE TABLE IF NOT EXISTS USER_TB (
			|_id INTEGER PRIMARY KEY AUTOINCREMENT,
			|user_name VARCHAR(20) NOT NULL,
			|birthday CHAR(10),
			|email VARCHAR(255),
			|join_dt CHAR(10) NOT NULL,
			|sns_type INTEGER(1) NOT NULL,
			|gender INTEGER(1),
			|phone_num VARCHAR(15)
			|)
		""".trimMargin()

		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun createTableLabel(db: SQLiteDatabase) {
		val sql = """
			|CREATE TABLE IF NOT EXISTS LABEL_TB (
			|_id INTEGER PRIMARY KEY AUTOINCREMENT,
			|label_user INTEGER NOT NULL,
			|label_title VARCHAR(10) NOT NULL,
			|label_color CHAR(7) NOT NULL,
			|FOREIGN KEY (label_user) REFERENCES USER_TB(_id) ON DELETE CASCADE
			|)
		""".trimMargin()

		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun createTableAlarm(db: SQLiteDatabase) {
		val sql = """
			|CREATE TABLE IF NOT EXISTS ALARM_TB (
			|_id INTEGER PRIMARY KEY AUTOINCREMENT,
			|alarm_title VARCHAR(64) NOT NULL,
			|alarm_user INTEGER NOT NULL,
			|alarm_label INTEGER NOT NULL,
			|alarm_start_dt CHAR(10) NOT NULL,
			|alarm_end_dt CHAR(10) NOT NULL,
			|alarm_times VARCHAR(128) NOT NULL,
			|alarm_repeats VARCHAR(32) NOT NULL,
			|alarm_enabled INTEGER(1) NOT NULL,
			|alarm_created_dt CHAR(10) NOT NULL,
			|alarm_edited_dt CHAR(10) NOT NULL,
			|FOREIGN KEY (alarm_user) REFERENCES USER_TB(_id) ON DELETE CASCADE,
			|FOREIGN KEY (alarm_label) REFERENCES ALARM_TB(_id) ON DELETE CASCADE
			|)
		""".trimMargin()

		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun createTableImage(db: SQLiteDatabase) {
		val sql = """
			|CREATE TABLE IF NOT EXISTS IMAGE_TB (
			|_id INTEGER PRIMARY KEY AUTOINCREMENT,
			|img_alarm INTEGER NOT NULL,
			|img LONGBLOB NOT NULL,
			|img_created_dt CHAR(10) NOT NULL,
			|FOREIGN KEY (img_alarm) REFERENCES ALARM_TB(_id) ON DELETE CASCADE
			|)
		""".trimMargin()

		try {
			db.execSQL(sql)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun createTableCare(db: SQLiteDatabase) {
		val sql = """
			|CREATE TABLE IF NOT EXISTS CARE_TB (
			|_id INTEGER PRIMARY KEY AUTOINCREMENT,
			|user_id INTEGER NOT NULL,
			|care_user INTEGER NOT NULL，
			|care_start_dt CHAR(10) NOT NULL,
			|FOREIGN KEY (user_id) REFERENCES USER_TB(_id) ON DELETE CASCADE,
			|FOREIGN KEY (care_user) REFERENCES USER_TB(_id) ON DELETE CASCADE
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