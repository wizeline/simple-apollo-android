package com.wizeline.simpleapollosample.usecases

import com.wizeline.simpleapollosample.data.ApiGateway
import com.wizeline.simpleapollosample.entities.Comment
import com.wizeline.simpleapollosample.entities.responses.ApiResponse

class CreateComment(
    private val apiGateway: ApiGateway
) {
    suspend operator fun invoke(
        postId: String,
        text: String
    ): ApiResponse<Comment> =
        apiGateway.createComment(postId, text)
}
