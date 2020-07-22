package com.wizeline.simpleapollosample.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wizeline.simpleapollo.exceptions.EmptyResponse
import com.wizeline.simpleapollosample.entities.Comment
import com.wizeline.simpleapollosample.entities.Post
import com.wizeline.simpleapollosample.entities.responses.ApiResponse
import com.wizeline.simpleapollosample.graphql.type.CommentOrderBy
import com.wizeline.simpleapollosample.usecases.CreateComment
import com.wizeline.simpleapollosample.usecases.GetPost
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PostViewModel(
    private val backgroundDispatcher: CoroutineContext,
    private val getPost: GetPost,
    private val createComment: CreateComment
) : ViewModel() {

    private val postDetails = MutableLiveData<PostViewState>()
    val post: LiveData<PostViewState>
        get() = postDetails

    private val commentCreation = MutableLiveData<CommentViewState>()
    val comment: LiveData<CommentViewState>
        get() = commentCreation

    fun getPostById(
        postId: String,
        commentsFirst: Int,
        commentsOrderBy: CommentOrderBy
    ) = viewModelScope.launch(backgroundDispatcher) {
        when (val response = getPost(postId, commentsFirst, commentsOrderBy)) {
            is ApiResponse.Success -> postDetails.postValue(PostViewState.Success(response.data))
            is ApiResponse.Error -> when (val error = response.error) {
                is EmptyResponse -> postDetails.postValue(PostViewState.Empty)
                else -> postDetails.postValue(PostViewState.Error(error))
            }
        }
    }

    fun createNewComment(
        postId: String,
        text: String
    ) = viewModelScope.launch(backgroundDispatcher) {
        when (val response = createComment(postId, text)) {
            is ApiResponse.Success -> commentCreation.postValue(CommentViewState.Success(response.data))
            is ApiResponse.Error -> commentCreation.postValue(CommentViewState.Error(response.error))
        }
    }

    sealed class PostViewState {
        class Success(val post: Post) : PostViewState()
        object Empty : PostViewState()
        class Error(val throwable: Throwable) : PostViewState()
    }

    sealed class CommentViewState {
        class Success(val comment: Comment) : CommentViewState()
        class Error(val throwable: Throwable) : CommentViewState()
    }
}
