package com.wizeline.simpleapollosample.usecases

import com.wizeline.simpleapollosample.data.ApiGateway
import com.wizeline.simpleapollosample.entities.Post
import com.wizeline.simpleapollosample.entities.responses.ApiResponse
import com.wizeline.simpleapollosample.graphql.type.PostOrderBy

class AllPosts(
    private val apiGateway: ApiGateway
) {
    suspend operator fun invoke(
        first: Int,
        orderBy: PostOrderBy
    ): ApiResponse<List<Post>> =
        apiGateway.allPosts(first, orderBy)
}
