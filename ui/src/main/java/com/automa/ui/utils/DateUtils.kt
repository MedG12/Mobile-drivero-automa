package com.automa.ui.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    fun formatDate(sourceDate: String, sourcePattern: String, expectedFormat: String): String {
        return try {
            val sourceFormat = SimpleDateFormat(sourcePattern)
            val parsedDate: Date = sourceFormat.parse(sourceDate)
            val expected = SimpleDateFormat(expectedFormat)
            expected.format(parsedDate)
        } catch (e: Exception) {
            sourceDate
        }
    }

    fun formatDate(date: Date, expectedFormat: String): String {
        val expected = SimpleDateFormat(expectedFormat)
        return expected.format(date)
    }

    fun parseDate(sourceDate: String, sourcePattern: String): Long {
        val sourceFormat = SimpleDateFormat(sourcePattern)
        val parsedDate: Date = sourceFormat.parse(sourceDate)
        return parsedDate.time
    }
}