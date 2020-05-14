package com.calc.historyDB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${HistoryContract.HistoryEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${HistoryContract.HistoryEntry.COLUMN_EXPRESSION} TEXT," +
            "${HistoryContract.HistoryEntry.COLUMN_VALUE} TEXT," +
            "${HistoryContract.HistoryEntry.COLUMN_TIME} INTEGER)"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${HistoryContract.HistoryEntry.TABLE_NAME}"


class CalculationHistoryDBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION ) {

    val TAG = "CalculationHistoryDBHel"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        Log.i(TAG, "onCreate")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }


    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "History.db"
    }
}

