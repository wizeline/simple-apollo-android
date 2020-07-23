package com.wizeline.simpleapollo.api.constants

enum class TimeUnit(
    val javaTimeUnit: java.util.concurrent.TimeUnit
) {
    SECONDS(java.util.concurrent.TimeUnit.SECONDS),
    MINUTES(java.util.concurrent.TimeUnit.MINUTES),
    HOURS(java.util.concurrent.TimeUnit.HOURS),
    DAYS(java.util.concurrent.TimeUnit.DAYS)
}
