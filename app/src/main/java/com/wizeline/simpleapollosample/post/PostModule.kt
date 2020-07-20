package com.wizeline.simpleapollosample.post

import com.wizeline.simpleapollosample.utils.BACKGROUND_DISPATCHER
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val postModule = module {
    viewModel {
        PostViewModel(
            get(named(BACKGROUND_DISPATCHER)),
            get(),
            get()
        )
    }
}
