package com.wizeline.simpleapollo.api.constants

enum class DateTimePatterns(
    val pattern: String
) {
    RFC822("yyyy-MM-dd'T'HH:mm:ss"),
    RFC822_MILLIS("yyyy-MM-dd'T'HH:mm:ss.SSS"),
    RFC822_MICROS("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"),
    RFC822_TZ("yyyy-MM-dd'T'HH:mm:ssZ"),
    RFC822_MILLIS_TZ("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
    RFC822_MICROS_TZ("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"),
    RFC822_UTC("yyyy-MM-dd'T'HH:mm:ss'Z'"),
    RFC822_MILLIS_UTC("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
    RFC822_MICROS_UTC("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"),
    ISO8601_TZ("yyyy-MM-dd'T'HH:mm:ssXXX"),
    ISO8601_MILLIS_TZ("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
    ISO8601_MICROS_TZ("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
}
