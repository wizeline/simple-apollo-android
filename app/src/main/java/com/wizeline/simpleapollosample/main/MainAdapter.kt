package com.wizeline.simpleapollosample.main

import androidx.recyclerview.widget.RecyclerView
import com.wizeline.simpleapollo.utils.extensions.toTimeAgo
import com.wizeline.simpleapollosample.R
import com.wizeline.simpleapollosample.entities.Post
import com.wizeline.simpleapollosample.ui.Adapter
import com.wizeline.simpleapollosample.ui.ViewHolder
import kotlinx.android.synthetic.main.item_post.view.*

class MainAdapter(
    private val clickListener: (Post) -> (Unit)
) : Adapter<Post>() {

    override fun bind(item: Post, viewHolder: ViewHolder) {
        viewHolder.itemView.postTitle.text = item.title
        viewHolder.itemView.postAuthor.apply {
            text = context.getString(
                R.string.post__author,
                item.user?.name ?: context.getString(R.string.unknown)
            )
        }
        viewHolder.itemView.postLastUpdate.apply {
            text = context.getString(
                R.string.post__last_update,
                item.updatedAt?.toTimeAgo() ?: context.getString(R.string.unknown)
            )
        }
        viewHolder.itemView.setOnClickListener { clickListener(item) }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_post
}
