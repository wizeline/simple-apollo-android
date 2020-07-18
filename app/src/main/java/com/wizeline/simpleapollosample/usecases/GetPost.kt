package com.wizeline.simpleapollosample.usecases

import com.wizeline.simpleapollosample.data.ApiGateway
import com.wizeline.simpleapollosample.entities.Post
import com.wizeline.simpleapollosample.entities.responses.ApiResponse
import com.wizeline.simpleapollosample.graphql.type.CommentOrderBy

class GetPost(
    private val apiGateway: ApiGateway
) {
    suspend operator fun invoke(
        id: String,
        commentsFirst: Int,
        commentsOrderBy: CommentOrderBy
    ): ApiResponse<Post> =
        apiGateway.getPost(id, commentsFirst, commentsOrderBy)
}
