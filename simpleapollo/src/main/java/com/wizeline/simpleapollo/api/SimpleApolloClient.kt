package com.wizeline.simpleapollo.api

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.http.ApolloHttpCache
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore
import com.wizeline.simpleapollo.api.cache.CacheConfiguration
import com.wizeline.simpleapollo.api.constants.TimeUnit
import com.wizeline.simpleapollo.exceptions.ExpectedParameterError
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

class SimpleApolloClient private constructor(
    private val apolloClient: ApolloClient,
    private val useCache: Boolean,
    private val cacheConfiguration: CacheConfiguration,
    private val isDebug: Boolean
) {

    data class Builder(
        private var context: Context? = null,
        private var serverUrl: String? = null,
        private var timeOutValue: Long = 30,
        private var timeOutUnit: TimeUnit = TimeUnit.SECONDS,
        private var useCache: Boolean = false,
        private var cacheConfiguration: CacheConfiguration = CacheConfiguration(),
        private var isDebug: Boolean = false
    ) {

        fun context(context: Context) = apply {
            this.context = context
        }

        fun serverUrl(serverUrl: String) = apply {
            this.serverUrl = serverUrl
        }

        fun connectionTimeOut(value: Long, timeUnit: TimeUnit) = apply {
            this.timeOutValue = value
            this.timeOutUnit = timeOutUnit
        }

        fun enableCache(cacheConfiguration: CacheConfiguration?) = apply {
            this.useCache = true
            cacheConfiguration?.also { this.cacheConfiguration = it }
        }

        fun isDebug(isDebug: Boolean) = apply {
            this.isDebug = isDebug
        }

        fun build(): SimpleApolloClient {
            if (this.context == null) {
                throw ExpectedParameterError("$this requires a context")
            }
            val graphqlServerUrl = this.serverUrl?.let { it } ?: throw ExpectedParameterError("$this requires a server url")
            val apolloClient = ApolloClient.builder()
                .okHttpClient(this.getOkHttpClient())
                .serverUrl(graphqlServerUrl)
            if (this.useCache) {
                apolloClient.httpCache(this.getApolloHttpCache())
            }
            return SimpleApolloClient(
                apolloClient = apolloClient.build(),
                useCache = this.useCache,
                cacheConfiguration = this.cacheConfiguration,
                isDebug = this.isDebug
            )
        }

        private fun getOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().setLevel(
                        if (this.isDebug)
                            HttpLoggingInterceptor.Level.BODY
                    else
                            HttpLoggingInterceptor.Level.NONE
                    )
                )
                .connectTimeout(this.timeOutValue, this.timeOutUnit.javaTimeUnit)
                .readTimeout(this.timeOutValue, this.timeOutUnit.javaTimeUnit)
                .callTimeout(this.timeOutValue, this.timeOutUnit.javaTimeUnit)
                .writeTimeout(this.timeOutValue, this.timeOutUnit.javaTimeUnit)
                .build()

        private fun getApolloHttpCache(): ApolloHttpCache = this.context?.let {
            ApolloHttpCache(
                DiskLruHttpCacheStore(
                    File(it.cacheDir, this.cacheConfiguration.fileName),
                    this.cacheConfiguration.cacheSize
                )
            )
        } ?: throw ExpectedParameterError("$this requires a context")
    }

    fun apolloClient(): ApolloClient = this.apolloClient
}
