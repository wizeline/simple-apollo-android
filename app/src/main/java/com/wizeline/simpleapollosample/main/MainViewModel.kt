package com.wizeline.simpleapollosample.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wizeline.simpleapollo.exceptions.EmptyResponse
import com.wizeline.simpleapollosample.entities.Post
import com.wizeline.simpleapollosample.entities.responses.ApiResponse
import com.wizeline.simpleapollosample.graphql.type.PostOrderBy
import com.wizeline.simpleapollosample.usecases.AllPosts
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel(
    private val backgroundDispatcher: CoroutineContext,
    private val allPosts: AllPosts
) : ViewModel() {

    private val postsList = MutableLiveData<PostsViewState>()
    val posts: LiveData<PostsViewState>
        get() = postsList

    fun getAllPosts() = viewModelScope.launch(backgroundDispatcher) {
        when (val response = allPosts(
            first = 50,
            orderBy = PostOrderBy.UPDATEDAT_DESC
        )) {
            is ApiResponse.Success -> postsList.postValue(PostsViewState.Success(response.data))
            is ApiResponse.Error -> when (val error = response.error) {
                is EmptyResponse -> postsList.postValue(PostsViewState.Empty)
                else -> postsList.postValue(PostsViewState.Error(error))
            }
        }
    }

    sealed class PostsViewState {
        class Success(val posts: List<Post>) : PostsViewState()
        object Empty : PostsViewState()
        class Error(val throwable: Throwable) : PostsViewState()
    }
}
