package com.wizeline.simpleapollosample.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.lifecycle.Observer
import com.wizeline.simpleapollo.utils.extensions.toTimeAgo
import com.wizeline.simpleapollosample.R
import com.wizeline.simpleapollosample.entities.Post
import com.wizeline.simpleapollosample.graphql.type.CommentOrderBy
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.content_post.*
import org.koin.android.viewmodel.ext.android.viewModel

class PostActivity : AppCompatActivity() {

    private val postViewModel: PostViewModel by viewModel()

    private val commentAdapter: CommentAdapter = CommentAdapter()

    private lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        setSupportActionBar(toolbar)

        setupUi()
        subscribePost()
        subscribeComment()
    }

    override fun onResume() {
        super.onResume()
        loadPost()
    }

    private fun subscribePost() = postViewModel.post.observe(this, Observer {
        when (val response = it) {
            is PostViewModel.PostViewState.Success -> {
                post = response.post.copy(
                    id = post.id,
                    title = post.title,
                    updatedAt = post.updatedAt,
                    user = post.user
                )
                postText.text = post.text
                post.comments?.also { comments ->
                    commentAdapter.items = comments.toMutableList()
                }
            }
            else -> finish()
        }
    })

    private fun subscribeComment() = postViewModel.comment.observe(this, Observer {
        when (val response = it) {
            is PostViewModel.CommentViewState.Success -> {
                post.comments?.toMutableList()?.asReversed()?.apply {
                    add(response.comment)
                    commentAdapter.items = this.asReversed()
                }
                newCommentText.text = Editable.Factory.getInstance().newEditable("")
            }
            is PostViewModel.CommentViewState.Error -> Toast.makeText(
                this,
                response.throwable.localizedMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    })

    private fun setupUi() {
        intent.extras?.getParcelable<Post>("post")?.also { post ->
            this.post = post
        } ?: finish()
        commentsRecycler.apply {
            adapter = commentAdapter
        }
        addComment.setOnClickListener {
            createComment()
        }
        if (this::post.isInitialized) {
            supportActionBar?.apply {
                title = post.title
                subtitle = post.user?.let { user ->
                    getString(
                        R.string.post__author,
                        user.name
                    )
                } ?: getString(
                    R.string.post__last_update,
                    post.updatedAt?.toTimeAgo() ?: getString(R.string.unknown)
                )
            }
        }
    }

    private fun loadPost() {
        if (this::post.isInitialized) {
            postViewModel.getPostById(
                postId = post.id,
                commentsFirst = 5,
                commentsOrderBy = CommentOrderBy.UPDATEDAT_DESC
            )
        }
    }

    private fun createComment() {
        if (this::post.isInitialized) {
            postViewModel.createNewComment(
                postId = post.id,
                text = newCommentText.text.toString()
            )
        }
    }
}
