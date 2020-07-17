package com.wizeline.simpleapollo.utils.extensions

import android.os.Build
import android.text.format.DateUtils
import com.wizeline.simpleapollo.api.constants.DateTimePatterns
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

fun String.fromIso8601ToRfc822(): String =
    "${this.substring(0, this.lastIndexOf(":") + 1)}${this.substring(this.lastIndexOf(":") + 1, this.length + 1)}"

fun String.toMillis(dateTimePatterns: DateTimePatterns): Long = SimpleDateFormat(
    dateTimePatterns.pattern,
    Locale.getDefault()
).let {
    it.parse(this)?.time ?: 0
}

fun Long.toDate(): Date = Date(this)

fun String.toTimeAgo(dateTimePatterns: DateTimePatterns): String = DateUtils.getRelativeTimeSpanString(
    toMillis(dateTimePatterns),
    System.currentTimeMillis(),
    DateUtils.MINUTE_IN_MILLIS,
    DateUtils.FORMAT_ABBREV_RELATIVE
).toString()

fun Date.toTimeAgo(): String = DateUtils.getRelativeTimeSpanString(
    this.time,
    System.currentTimeMillis(),
    DateUtils.MINUTE_IN_MILLIS,
    DateUtils.FORMAT_ABBREV_RELATIVE
).toString()

fun String.toDate(dateTimePatterns: DateTimePatterns): Date? = SimpleDateFormat(
    dateTimePatterns.pattern,
    Locale.getDefault()
).let {
    it.parse(this)
}

fun Date.toSimpleApolloString(dateTimePatterns: DateTimePatterns): String = SimpleDateFormat(
    dateTimePatterns.pattern,
    Locale.getDefault()
).let {
    it.format(this)
}

fun String.toJson(): JSONObject = JSONObject(
    this.substring(
        this.indexOf("{"),
        this.lastIndexOf("}") + 1
    )
        .replace("\\\"", "\"")
)
