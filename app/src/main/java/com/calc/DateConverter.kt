package com.calc

import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    private var dateFormat = object: SimpleDateFormat("d MMMM y") {}
    private val today = dateFormat.format(Date())
    private val date = Date()

    fun convertDate(time: Long): String {
        date.time = time
        val formattedDate = dateFormat.format(date)
        //TODO Make string resource depending locale
        return if (formattedDate == today) "Сегодня" else formattedDate
    }
}