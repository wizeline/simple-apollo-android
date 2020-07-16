package com.wizeline.simpleapollo

import android.app.Application
import com.wizeline.simpleapollo.utils.SimpleApolloDebuger
import timber.log.Timber

class SimpleApollo(application: Application) {

    companion object {
        private lateinit var instance: SimpleApollo

        fun initialize(application: Application) {
            instance = SimpleApollo(application)
        }
    }

    init {
        initializeLogger()
    }

    private fun initializeLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(SimpleApolloDebuger())
        }
    }
}
