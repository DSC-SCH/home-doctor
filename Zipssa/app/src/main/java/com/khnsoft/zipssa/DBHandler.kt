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
		val DB_NAME = "Zipssa.db"
		val DB_VERSION = 1

		fun open(context: Context?): DBHandler {
			return DBHandler(context)
		}
	}

	fun updateTables(userId: Int) : Boolean {
		// TODO("Get CHECK_TB from server")
		val jServer = JsonObject()

		mDB = mHelper.writableDatabase

		val sql = "SELECT * FROM CHECK_TB WHERE user_id=${userId}"
		val lRet = execResult(sql)
		if (lRet.size() > 0) {
			val jLocal = lRet[0] as JsonObject
			for (tb in jLocal.keySet()) {
				if (tb == "user_id") continue
				if (jServer[tb].asInt != jLocal[tb].asInt) {
					// TODO("Update table")
				}
			}
			return true
		}
		return false
	}

	fun isUserExists(type: String, uid: String) {

	}

	fun execNonResult(sql: String): Boolean{
		mDB = mHelper.writableDatabase

		try {
			mDB.execSQL(sql)
			return true
		} catch (e: Exception) {
			e.printStackTrace()
		}
		return false
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