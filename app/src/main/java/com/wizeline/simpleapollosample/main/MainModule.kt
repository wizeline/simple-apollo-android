package com.wizeline.simpleapollosample.main

import com.wizeline.simpleapollosample.utils.BACKGROUND_DISPATCHER
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {
    viewModel {
        MainViewModel(
            get(named(BACKGROUND_DISPATCHER)),
            get()
        )
    }
}
