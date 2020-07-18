package com.wizeline.simpleapollosample.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class Adapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: MutableList<T> = emptyList<T>().toMutableList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    fun appendItems(items: List<T>) {
        val currentSize = this.items.size
        this.items.addAll(items)
        notifyItemRangeChanged(currentSize, this.items.size)
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    abstract fun bind(item: T, viewHolder: ViewHolder)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        bind(this.items[position], holder as ViewHolder)

    override fun getItemCount(): Int = items.count()
}
