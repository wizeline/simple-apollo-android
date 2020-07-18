package com.wizeline.simpleapollosample.utils

import com.wizeline.simpleapollosample.entities.Comment
import com.wizeline.simpleapollosample.entities.Post
import com.wizeline.simpleapollosample.entities.User
import com.wizeline.simpleapollosample.graphql.AllPostsQuery
import com.wizeline.simpleapollosample.graphql.CreateCommentMutation
import com.wizeline.simpleapollosample.graphql.GetPostQuery

fun AllPostsQuery.AllPost.toApiModel(): Post = Post(
    id = id,
    title = title,
    updatedAt = updatedAt,
    user = user?.toApiModel()
)

fun AllPostsQuery.User.toApiModel(): User = User(
    id = id,
    name = name
)

fun GetPostQuery.Post.toApiModel(): Post = Post(
    text = text,
    comments = comments?.mapNotNull { it.toApiModel() }
)

fun GetPostQuery.Comment.toApiModel(): Comment = Comment(
    id = id,
    text = text,
    updatedAt = updatedAt,
    user = user?.toApiModel()
)

fun GetPostQuery.User.toApiModel(): User = User(
    id = id,
    name = name
)

fun CreateCommentMutation.CreateComment.toApiModel(): Comment = Comment(
    id = id
)
