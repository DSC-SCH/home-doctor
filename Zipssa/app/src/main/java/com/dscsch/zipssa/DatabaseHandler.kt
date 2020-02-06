package com.dscsch.zipssa

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
	SQLiteOpenHelper(context, name, factory, version) {

	override fun onCreate(db: SQLiteDatabase?) {

	}

	override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

	}

	fun createTableAlarms(db: SQLiteDatabase?) {
		val sql = """CREATE TABLE IF NOT EXISTS ALARMS (
			|_ID INT PRIMARY KEY AUTO_INCREMENT,
			|
		""".trimMargin()
	}
}