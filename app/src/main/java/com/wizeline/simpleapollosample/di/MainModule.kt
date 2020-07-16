package com.wizeline.simpleapollosample.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

const val BACKGROUND_DISPATCHER = "backgroundDispatcher"

val mainModule = module {

    single<CoroutineContext>(named(BACKGROUND_DISPATCHER)) {
        Dispatchers.Default
    }
}
