package com.calc.historyDB

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.calc.ui.Expression

object HistoryManager {


    var context: Context? = null
    set(value) {
        if (value != null) dBHelper = CalculationHistoryDBHelper(value)
    field = value}

    private lateinit var dBHelper: CalculationHistoryDBHelper


    fun insert(expression: Expression) {
        // Gets the data repository in write mode
        val db = dBHelper.writableDatabase

// Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(HistoryContract.HistoryEntry.COLUMN_EXPRESSION, expression.expression)
            put(HistoryContract.HistoryEntry.COLUMN_VALUE, expression.value)
            put(HistoryContract.HistoryEntry.COLUMN_TIME, expression.calendar)
        }

// Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(HistoryContract.HistoryEntry.TABLE_NAME, null, values)
        db.close()
    }


    fun query(): ArrayList<Expression> {
        val db = dBHelper.readableDatabase


        val projection = arrayOf(BaseColumns._ID, HistoryContract.HistoryEntry.COLUMN_EXPRESSION, HistoryContract.HistoryEntry.COLUMN_VALUE, HistoryContract.HistoryEntry.COLUMN_TIME)


        val selection = null
        val selectionArgs = null

        var historyArray = ArrayList<Expression>()



        val cursor = db.query(
            HistoryContract.HistoryEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        val idColumnIndex = cursor.getColumnIndex(BaseColumns._ID)
        val expressionColumnIndex = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_EXPRESSION)
        val valueColumnIndex = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_VALUE)
        val timeColumnIndex = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_TIME)


        while (cursor.moveToNext()) {
            // Используем индекс для получения строки или числа

            val currentID = cursor.getInt(idColumnIndex)
            val currentExpression = cursor.getString(expressionColumnIndex)
            val currentValue = cursor.getString(valueColumnIndex)
            val currentTime = cursor.getLong(timeColumnIndex)

            historyArray.add(Expression(currentExpression, currentValue, currentTime))



        }
        db.close()
        return historyArray



    }

    fun clearHistory() {
        val db = dBHelper.writableDatabase
        db.delete(HistoryContract.HistoryEntry.TABLE_NAME, null, null)
    }
}