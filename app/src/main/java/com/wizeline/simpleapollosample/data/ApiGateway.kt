package com.wizeline.simpleapollosample.data

import com.wizeline.simpleapollosample.entities.Comment
import com.wizeline.simpleapollosample.entities.Post
import com.wizeline.simpleapollosample.entities.responses.ApiResponse
import com.wizeline.simpleapollosample.graphql.GetPostQuery
import com.wizeline.simpleapollosample.graphql.type.CommentOrderBy
import com.wizeline.simpleapollosample.graphql.type.PostOrderBy

interface ApiGateway {

    suspend fun allPosts(
        first: Int,
        orderBy: PostOrderBy
    ): ApiResponse<List<Post>>

    suspend fun getPost(
        id: String,
        commentsFirst: Int,
        commentsOrderBy: CommentOrderBy
    ): ApiResponse<Post>

    suspend fun createComment(
        postId: String,
        text: String
    ): ApiResponse<Comment>
}
