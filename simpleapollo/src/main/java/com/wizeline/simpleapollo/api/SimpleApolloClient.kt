package com.wizeline.simpleapollo.api

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.ScalarType
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.cache.http.ApolloHttpCache
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.request.RequestHeaders
import com.wizeline.simpleapollo.api.cache.CacheConfiguration
import com.wizeline.simpleapollo.api.constants.AUTHORIZATION_HEADER
import com.wizeline.simpleapollo.api.constants.TimeUnit
import com.wizeline.simpleapollo.exceptions.ExpectedParameterError
import com.wizeline.simpleapollo.models.Response
import com.wizeline.simpleapollo.utils.extensions.processResponse
import kotlinx.coroutines.awaitAll
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
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
        private var customTypeAdapters: Map<ScalarType, CustomTypeAdapter<*>>? = null,
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

        fun addCustomTypeAdapters(customTypeAdapters: Map<ScalarType, CustomTypeAdapter<*>>) = apply {
            this.customTypeAdapters = customTypeAdapters
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
            this.customTypeAdapters?.takeUnless { it.isNullOrEmpty() }?.forEach { (scalarType, customTypeAdapter) ->
                apolloClient.addCustomTypeAdapter(scalarType, customTypeAdapter)
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

    suspend fun <T, Q: Query<*, T, *>> query(
        query: Q,
        authorizationToken: String? = null,
        httpCachePolicy: HttpCachePolicy.Policy? = null
    ): Response<T> =
        try {
            val apolloQuery = this.apolloClient.query(query)
                .httpCachePolicy(
                    if (this.isDebug.not())
                        if (this.useCache)
                            httpCachePolicy ?: HttpCachePolicy.CACHE_FIRST.expireAfter(
                                this.cacheConfiguration.expireTime,
                                this.cacheConfiguration.expireUnit.javaTimeUnit
                            )
                else
                            HttpCachePolicy.NETWORK_ONLY
                else
                        HttpCachePolicy.NETWORK_ONLY
                )
            if (authorizationToken.isNullOrBlank().not()) {
                apolloQuery.requestHeaders(
                    RequestHeaders.Builder()
                        .addHeader(AUTHORIZATION_HEADER, authorizationToken)
                        .build()
                )
            }
            apolloQuery.toDeferred()
                .await()
                .processResponse(this.isDebug)
        } catch (e: Exception) {
            if (this.isDebug) {
                Timber.e(e)
            }
            Response.Failure(e)
        }

    suspend fun <T, M: Mutation<*, T, *>> mutate(
        mutation: M,
        authorizationToken: String? = null
    ): Response<T> =
        try {
            val apolloMutation = this.apolloClient.mutate(mutation)
            if (authorizationToken.isNullOrBlank().not()) {
                apolloMutation.requestHeaders(
                    RequestHeaders.Builder()
                        .addHeader(AUTHORIZATION_HEADER, authorizationToken)
                        .build()
                )
            }
            apolloMutation.toDeferred()
                .await()
                .processResponse(this.isDebug)
        } catch (e: Exception) {
            if (this.isDebug) {
                Timber.e(e)
            }
            Response.Failure(e)
        }
}
