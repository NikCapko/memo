package com.nikcapko.memo.core.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseItemViewHolder<out V : ViewBinding, in I>(
    val itemBinding: V
) : RecyclerView.ViewHolder(itemBinding.root) {
    abstract fun onBind(item: I)
}
