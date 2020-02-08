package com.dscsch.zipssa

import android.content.Context
import android.database.sqlite.SQLiteDatabase

class SharedDB(context: Context) {
	companion object {
		val DB_NAME = "HomeDoc.db"

		lateinit var database: SQLiteDatabase
		lateinit var helper: DBHelper
	}
}