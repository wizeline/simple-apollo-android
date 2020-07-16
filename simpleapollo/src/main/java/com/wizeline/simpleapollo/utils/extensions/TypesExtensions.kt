package com.wizeline.simpleapollo.utils.extensions

import android.os.Build
import android.text.format.DateUtils
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

private const val PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX"
private const val LEGACY_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"

fun String.toMillis(): Long = SimpleDateFormat(
    if (Build.VERSION.SDK_INT >= 24)
        PATTERN
    else
        LEGACY_PATTERN,
    Locale.getDefault()
).let {
    it.parse(
        if (Build.VERSION.SDK_INT >=24)
            this
        else
            "${this.substring(0, this.lastIndexOf(":") + 1)}00"
    )?.time ?: 0
}

fun Long.toDate(): Date = Date(this)

fun String.toTimeAgo() = DateUtils.getRelativeTimeSpanString(
    toMillis(),
    System.currentTimeMillis(),
    DateUtils.MINUTE_IN_MILLIS,
    DateUtils.FORMAT_ABBREV_RELATIVE
).toString()

fun Date.toTimeAgo() = DateUtils.getRelativeTimeSpanString(
    this.time,
    System.currentTimeMillis(),
    DateUtils.MINUTE_IN_MILLIS,
    DateUtils.FORMAT_ABBREV_RELATIVE
).toString()

fun String.toDate(): Date? = SimpleDateFormat(
    if (Build.VERSION.SDK_INT >= 24)
        PATTERN
    else
        LEGACY_PATTERN,
    Locale.getDefault()
).let {
    it.parse(
        if (Build.VERSION.SDK_INT >= 24)
            this
        else
            "${this.substring(0, this.lastIndexOf(":") + 1)}00"
    )
}

fun Date.toSimpleApolloString(): String = SimpleDateFormat(
    if (Build.VERSION.SDK_INT >= 24)
        PATTERN
    else
        LEGACY_PATTERN,
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
