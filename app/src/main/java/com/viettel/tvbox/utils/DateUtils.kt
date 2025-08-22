package com.viettel.tvbox.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    /**
     * Convert a date string from "yyyy-MM-dd HH:mm:ss.SSS" to "dd/MM/yyyy".
     * Returns the formatted string, or the original if parsing fails.
     */
    fun formatDate(input: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = parser.parse(input)
            if (date != null) formatter.format(date) else input
        } catch (e: Exception) {
            input
        }
    }
}

