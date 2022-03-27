package com.app.topupmama.utils.dateFormater

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object FormatDate {
    fun getFormattedFullDateString(dateStr: String): String? {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"/*'Z'*/, Locale.ENGLISH)
            val formattedDate = formatter.parse(dateStr)
            return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(formattedDate ?: Date())
        } catch (e: Exception){
            ""
        }
    }
}