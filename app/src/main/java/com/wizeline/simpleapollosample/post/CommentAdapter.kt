package com.wizeline.simpleapollosample.post

import com.wizeline.simpleapollo.utils.extensions.toTimeAgo
import com.wizeline.simpleapollosample.R
import com.wizeline.simpleapollosample.entities.Comment
import com.wizeline.simpleapollosample.ui.Adapter
import com.wizeline.simpleapollosample.ui.ViewHolder
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter : Adapter<Comment>() {

    override fun bind(item: Comment, viewHolder: ViewHolder) {
        viewHolder.itemView.commentAuthor.apply {
            text = context.getString(
                R.string.comment__author,
                item.user?.name ?: context.getString(R.string.unknown)
            )
        }
        viewHolder.itemView.commentPublishedAt.apply {
            text = context.getString(
                R.string.comment_published_at,
                item.updatedAt?.toTimeAgo() ?: context.getString(R.string.unknown)
            )
        }
        viewHolder.itemView.commentText.apply {
            text = item.text
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_comment
}
