package com.calc

import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    var dateFormat = object: SimpleDateFormat("d MMMM y") {}
    val today = dateFormat.format(Date())
    val date = Date()

    fun convertDate(time: Long): String {
        date.time = time
        //TODO Make string resource depending locale
        return if (dateFormat.format(date) == today) "Сегодня" else dateFormat.format(date)
    }
}