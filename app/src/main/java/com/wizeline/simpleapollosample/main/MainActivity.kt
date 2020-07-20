package com.wizeline.simpleapollosample.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.wizeline.simpleapollosample.R
import com.wizeline.simpleapollosample.entities.Post
import com.wizeline.simpleapollosample.post.PostActivity
import com.wizeline.viewstate.State
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    private val mainAdapter: MainAdapter =
        MainAdapter({ post ->
            postOnClickListener(post)
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUi()
        subscribePosts()
    }

    override fun onResume() {
        super.onResume()
        getAllPosts()
    }

    private fun subscribePosts() = mainViewModel.posts.observe(this, Observer {
        when (val response = it) {
            is MainViewModel.PostsViewState.Success -> {
                if (mainAdapter.itemCount == 0) {
                    mainViewState.setState(State.CONTENT)
                    mainAdapter.items = response.posts.toMutableList()
                }
            }
            is MainViewModel.PostsViewState.Empty -> {
                if (mainAdapter.itemCount == 0) {
                    mainViewState.setState(State.EMPTY)
                }
            }
            is MainViewModel.PostsViewState.Error -> {
                if (mainAdapter.itemCount == 0) {
                    mainViewState.setState(State.ERROR)
                    mainViewState.setErrorDescriptionText(response.throwable.localizedMessage)
                }
            }
        }
    })

    private fun setupUi() {
        mainRecycler.apply {
            adapter = mainAdapter
        }
        mainViewState.setOnRetryClickListener {
            getAllPosts()
        }
    }

    private fun getAllPosts() {
        if (mainAdapter.itemCount == 0) {
            mainViewState.setState(State.LOADING)
            mainViewModel.getAllPosts()
        }
    }

    private fun postOnClickListener(post: Post) {
        Intent(this, PostActivity::class.java).apply {
            putExtra("post", post)
            startActivity(this)
        }
    }
}
