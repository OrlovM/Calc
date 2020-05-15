package com.calc.historyDB

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.calc.common.Expression

object HistoryManager {


    var context: Context? = null
    set(value) {
        if (value != null) dBHelper = CalculationHistoryDBHelper(value)
    field = value}

    private lateinit var dBHelper: CalculationHistoryDBHelper


    fun insert(expression: Expression) {

        val db = dBHelper.writableDatabase

        val values = ContentValues().apply {
            put(HistoryContract.HistoryEntry.COLUMN_EXPRESSION, expression.expression)
            put(HistoryContract.HistoryEntry.COLUMN_VALUE, expression.value)
            put(HistoryContract.HistoryEntry.COLUMN_TIME, expression.calendar)
        }

        val newRowId = db.insert(HistoryContract.HistoryEntry.TABLE_NAME, null, values)
        db.close()
    }


    fun query(): ArrayList<Expression> {

        val db = dBHelper.readableDatabase
        val projection = arrayOf(BaseColumns._ID, HistoryContract.HistoryEntry.COLUMN_EXPRESSION, HistoryContract.HistoryEntry.COLUMN_VALUE, HistoryContract.HistoryEntry.COLUMN_TIME)
        var historyArray = ArrayList<Expression>()
        val cursor = db.query(
            HistoryContract.HistoryEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        val expressionColumnIndex = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_EXPRESSION)
        val valueColumnIndex = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_VALUE)
        val timeColumnIndex = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_TIME)

        while (cursor.moveToNext()) {

            val currentExpression = cursor.getString(expressionColumnIndex)
            val currentValue = cursor.getString(valueColumnIndex)
            val currentTime = cursor.getLong(timeColumnIndex)

            historyArray.add(
                Expression(
                    currentExpression,
                    currentValue,
                    currentTime
                )
            )
        }
        db.close()
        return historyArray
    }

    fun clearHistory() {
        val db = dBHelper.writableDatabase
        db.delete(HistoryContract.HistoryEntry.TABLE_NAME, null, null)
    }


}