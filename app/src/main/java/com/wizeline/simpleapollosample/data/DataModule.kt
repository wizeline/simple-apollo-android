package com.wizeline.simpleapollosample.data

import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.ScalarType
import com.wizeline.simpleapollo.SimpleApollo
import com.wizeline.simpleapollo.api.SimpleApolloClient
import org.koin.dsl.module

val dataModule = module {

    single<SimpleApolloClient> {
        SimpleApolloClient.Builder()
            .context(get())
            .serverUrl("https://api.graph.cool/simple/v1/ciyz901en4j590185wkmexyex")
            .enableCache()
            .build()
    }

    single<ApiGateway> {
        SimpleApolloApiGateway(
            get()
        )
    }
}
