package com.wizeline.simpleapollosample.main

import androidx.lifecycle.ViewModel
import com.wizeline.simpleapollosample.usecases.AllPosts
import kotlin.coroutines.CoroutineContext

class MainViewModel(
    private val backgroundDispatcher: CoroutineContext,
    private val allPosts: AllPosts
) : ViewModel() {
}
