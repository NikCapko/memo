package com.nikcapko.memo.ui.words.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikcapko.memo.base.ui.BaseItemViewHolder
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.databinding.ItemWordListBinding
import kotlin.properties.Delegates

internal class WordListAdapter(
    val onItemClick: (Int) -> Unit,
    val onEnableSound: (Int) -> Unit,
) : RecyclerView.Adapter<BaseItemViewHolder<ItemWordListBinding, Word>>() {

    var words: List<Word>? by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseItemViewHolder<ItemWordListBinding, Word> {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemWordListBinding.inflate(inflater, parent, false)
        return ItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(
        holder: BaseItemViewHolder<ItemWordListBinding, Word>,
        position: Int,
    ) {
        words?.getOrNull(position)?.let { holder.onBind(it) }
    }

    override fun getItemCount(): Int = words?.size ?: 0

    inner class ItemViewHolder(itemBinding: ItemWordListBinding) :
        BaseItemViewHolder<ItemWordListBinding, Word>(itemBinding) {

        init {
            itemView.setOnClickListener { onItemClick.invoke(absoluteAdapterPosition) }
            itemBinding.ivSoundWaves.setOnClickListener {
                onEnableSound(absoluteAdapterPosition)
            }
        }

        override fun onBind(item: Word) {
            itemBinding.apply {
                tvWordTitle.text = item.word
                tvWordTranslate.text = item.translation
            }
        }
    }
}
