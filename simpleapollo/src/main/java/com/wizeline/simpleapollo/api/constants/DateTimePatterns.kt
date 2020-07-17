package com.wizeline.simpleapollo.api.constants

enum class DateTimePatterns(
    val pattern: String
) {
    RFC822("yyyy-MM-dd'T'HH:mm:ssZ"),
    RFC822_MILLIS("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
    RFC822_MICROS("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"),
    ISO8601("yyyy-MM-dd'T'HH:mm:ssXXX"),
    ISO8601_MILLIS("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
    ISO8601_MICROS("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
}
