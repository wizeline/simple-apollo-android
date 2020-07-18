package com.wizeline.simpleapollosample.data

import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.ScalarType
import com.wizeline.simpleapollo.SimpleApollo
import com.wizeline.simpleapollo.api.SimpleApolloClient
import com.wizeline.simpleapollo.api.constants.DateTimePatterns
import com.wizeline.simpleapollo.api.customtypes.DateTimeCustomTypeAdapter
import com.wizeline.simpleapollosample.graphql.type.CustomType
import org.koin.dsl.module

val dataModule = module {

    single<SimpleApolloClient> {
        SimpleApolloClient.Builder()
            .context(get())
            .serverUrl("https://api.graph.cool/simple/v1/ciyz901en4j590185wkmexyex")
            .enableCache()
            .addCustomTypeAdapters(
                mapOf<ScalarType, CustomTypeAdapter<*>>(
                    Pair(CustomType.DATETIME, DateTimeCustomTypeAdapter(DateTimePatterns.RFC822_MILLIS))
                )
            )
            .isDebug()
            .build()
    }

    single<ApiGateway> {
        SimpleApolloApiGateway(
            get()
        )
    }
}
