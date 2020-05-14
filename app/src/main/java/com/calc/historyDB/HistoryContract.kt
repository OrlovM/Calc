package com.calc.historyDB

import android.provider.BaseColumns

object HistoryContract  {
    // Table contents are grouped together in an anonymous object.
    object HistoryEntry : BaseColumns {
        const val TABLE_NAME = "history"
        const val COLUMN_EXPRESSION = "expression"
        const val COLUMN_VALUE = "value"
        const val COLUMN_TIME = "time"
    }
}