package com.wizeline.simpleapollo.api.cache

import com.wizeline.simpleapollo.api.constants.TimeUnit

data class CacheConfiguration(
    val fileName: String = "SimpleApollo",
    val cacheSize: Long = 10485760,
    val expireTime: Long = 1,
    val expireUnit: TimeUnit = TimeUnit.DAYS
)
