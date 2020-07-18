package com.wizeline.simpleapollo.utils.extensions

import android.os.Build
import android.text.format.DateUtils
import com.wizeline.simpleapollo.api.constants.DateTimePatterns
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

fun String.fromIso8601ToRfc822(): String =
    "${this.substring(0, this.lastIndexOf(":") + 1)}${this.substring(this.lastIndexOf(":") + 1, this.length + 1)}"

fun String.toMillis(dateTimePattern: String, forceUtc: Boolean = false): Long = SimpleDateFormat(
    dateTimePattern,
    Locale.getDefault()
).let {
    if (forceUtc) {
        it.timeZone = TimeZone.getTimeZone("UTC")
    }
    it.parse(this)?.time ?: 0
}

fun Long.toDate(): Date = Date(this)

fun String.toTimeAgo(dateTimePattern: String, forceUtc: Boolean = false): String = DateUtils.getRelativeTimeSpanString(
    toMillis(dateTimePattern, forceUtc),
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

fun String.toDate(dateTimePattern: String, forceUtc: Boolean = false): Date? = SimpleDateFormat(
    dateTimePattern,
    Locale.getDefault()
).let {
    if (forceUtc) {
        it.timeZone = TimeZone.getTimeZone("UTC")
    }
    it.parse(this)
}

fun Date.toSimpleApolloString(dateTimePattern: String, forceUtc: Boolean = false): String = SimpleDateFormat(
    dateTimePattern,
    Locale.getDefault()
).let {
    if (forceUtc) {
        it.timeZone = TimeZone.getTimeZone("UTC")
    }
    it.format(this)
}

fun String.toJson(): JSONObject = JSONObject(
    this.substring(
        this.indexOf("{"),
        this.lastIndexOf("}") + 1
    )
        .replace("\\\"", "\"")
)
