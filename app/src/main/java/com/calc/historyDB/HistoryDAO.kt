package com.calc.historyDB

import android.content.ContentValues
import android.provider.BaseColumns
import com.calc.DateConverter
import com.calc.common.Expression
import java.util.*
import kotlin.collections.ArrayList

class HistoryDAO(private val dBHelper: CalculationHistoryDBHelper) {


    fun insert(expression: Expression) {

        val db = dBHelper.writableDatabase

        val values = ContentValues().apply {
            put(HistoryEntry.COLUMN_EXPRESSION, expression.expression)
            put(HistoryEntry.COLUMN_VALUE, expression.value)
            put(HistoryEntry.COLUMN_TIME, Date().time)
        }

        db.insert(HistoryEntry.TABLE_NAME, null, values)
        db.close()
    }


    fun query(): ArrayList<Expression> {

        val db = dBHelper.readableDatabase
        val projection = arrayOf(BaseColumns._ID, HistoryEntry.COLUMN_EXPRESSION, HistoryEntry.COLUMN_VALUE, HistoryEntry.COLUMN_TIME)
        val historyArray = ArrayList<Expression>()
        val cursor = db.query(
            HistoryEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        val expressionColumnIndex = cursor.getColumnIndex(HistoryEntry.COLUMN_EXPRESSION)
        val valueColumnIndex = cursor.getColumnIndex(HistoryEntry.COLUMN_VALUE)
        val timeColumnIndex = cursor.getColumnIndex(HistoryEntry.COLUMN_TIME)

        val converter = DateConverter()

        while (cursor.moveToNext()) {

            val currentExpression = cursor.getString(expressionColumnIndex)
            val currentValue = cursor.getString(valueColumnIndex)
            val currentTime = cursor.getLong(timeColumnIndex)

            historyArray.add(
                Expression(
                    currentExpression,
                    currentValue,
                    converter.convertDate(currentTime)
                )
            )
        }
        db.close()
        return historyArray
    }

    fun clearHistory() {
        val db = dBHelper.writableDatabase
        db.delete(HistoryEntry.TABLE_NAME, null, null)
    }
}