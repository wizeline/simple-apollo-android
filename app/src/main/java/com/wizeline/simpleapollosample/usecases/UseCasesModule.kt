package com.wizeline.simpleapollosample.usecases

import org.koin.dsl.module

val useCasesModule = module {
    factory { AllPosts(get()) }
    factory { GetPost(get()) }
    factory { CreateComment(get()) }
}
