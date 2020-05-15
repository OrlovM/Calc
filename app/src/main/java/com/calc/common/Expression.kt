package com.calc.common

import com.calc.common.HistoryItem

open class Expression(var expression: String, val value: String, var calendar: Long): HistoryItem {
}