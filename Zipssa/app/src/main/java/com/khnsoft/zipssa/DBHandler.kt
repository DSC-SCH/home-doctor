package com.khnsoft.zipssa

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.lang.Exception

class DBHandler(context: Context?) {
	lateinit var mDB: SQLiteDatabase
	val mHelper: MyDbOpenHelper

	init {
		mHelper = MyDbOpenHelper(context, DB_NAME, null, DB_VERSION)
	}

	companion object {
		val DB_NAME = "HomeDoc.db"
		val DB_VERSION = 1

		fun open(context: Context?): DBHandler {
			return DBHandler(context)
		}
	}

	fun execNonResult(sql: String): Int{
		mDB = mHelper.writableDatabase

		try {
			mDB.execSQL(sql)
			return 0
		} catch (e: Exception) {
			e.printStackTrace()
		}
		return 1
	}

	fun execResult(sql: String) : JsonArray {
		mDB = mHelper.readableDatabase

		val jRet = JsonArray()
		try {
			val cursor = mDB.rawQuery(sql, null)
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