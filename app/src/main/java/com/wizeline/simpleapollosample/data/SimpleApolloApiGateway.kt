package com.wizeline.simpleapollosample.data

import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.wizeline.simpleapollo.api.SimpleApolloClient
import com.wizeline.simpleapollo.exceptions.EmptyResponse
import com.wizeline.simpleapollo.exceptions.ResponseWithErrors
import com.wizeline.simpleapollo.models.Response
import com.wizeline.simpleapollosample.entities.Comment
import com.wizeline.simpleapollosample.entities.responses.ApiResponse
import com.wizeline.simpleapollosample.entities.Post
import com.wizeline.simpleapollosample.graphql.AllPostsQuery
import com.wizeline.simpleapollosample.graphql.CreateCommentMutation
import com.wizeline.simpleapollosample.graphql.GetPostQuery
import com.wizeline.simpleapollosample.graphql.type.CommentOrderBy
import com.wizeline.simpleapollosample.graphql.type.PostOrderBy
import com.wizeline.simpleapollosample.utils.toApiModel

class SimpleApolloApiGateway(
    private val simpleApolloClient: SimpleApolloClient
) : ApiGateway {

    override suspend fun allPosts(first: Int, orderBy: PostOrderBy): ApiResponse<List<Post>> =
        simpleApolloClient.query(
            query = com.wizeline.simpleapollosample.graphql.AllPostsQuery(
                first = first,
                orderBy = orderBy
            )
        ).let { response ->
            when (response) {
                is Response.Success -> response.data.allPosts.takeUnless { it.isNullOrEmpty() }?.let { posts ->
                    ApiResponse.Success(
                        posts.mapNotNull { it.toApiModel() }
                    )
                } ?: ApiResponse.Error(EmptyResponse())
                is Response.Failure -> ApiResponse.Error(response.error)
            }
        }

    override suspend fun getPost(
        id: String,
        commentsFirst: Int,
        commentsOrderBy: CommentOrderBy
    ): ApiResponse<Post> =
        simpleApolloClient.query(
            query = GetPostQuery(
                id = id,
                commentsFirst = commentsFirst,
                commentsOrderBy = commentsOrderBy
            ),
            httpCachePolicy = HttpCachePolicy.NETWORK_ONLY
        ).let { response ->
            when (response) {
                is Response.Success -> response.data.post?.let { post ->
                    ApiResponse.Success(
                        post.toApiModel()
                    )
                } ?: ApiResponse.Error(EmptyResponse())
                is Response.Failure -> ApiResponse.Error(response.error)
            }
        }

    override suspend fun createComment(postId: String, text: String): ApiResponse<Comment> =
        simpleApolloClient.mutate(
            mutation = CreateCommentMutation(
                postId = postId,
                text = text
            )
        ).let { response ->
            when (response) {
                is Response.Success -> response.data.createComment?.let { comment ->
                    ApiResponse.Success(
                        comment.toApiModel()
                    )
                } ?: ApiResponse.Error(EmptyResponse())
                is Response.Failure -> ApiResponse.Error(response.error)
            }
        }
}
