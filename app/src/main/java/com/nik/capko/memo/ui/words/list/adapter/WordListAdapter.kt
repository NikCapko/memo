package com.nik.capko.memo.ui.words.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.databinding.ItemWordListBinding
import kotlin.properties.Delegates

class WordListAdapter(
    val onItemClick: (Int) -> Unit,
    val onEnableSound: (Int) -> Unit,
) : RecyclerView.Adapter<WordListAdapter.ItemViewHolder>() {

    var words: List<Word>? by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemWordListBinding.inflate(inflater, parent, false)
        return ItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(words?.getOrNull(position))
    }

    override fun getItemCount(): Int = words?.size ?: 0

    inner class ItemViewHolder(private val itemBinding: ItemWordListBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick.invoke(absoluteAdapterPosition)
            }
            itemBinding.ivSoundWaves.setOnClickListener {
                onEnableSound.invoke(absoluteAdapterPosition)
            }
        }

        fun bind(word: Word?) {
            word?.let {
                itemBinding.tvWordTitle.text = it.word
                itemBinding.tvWordTranslate.text = it.translation
            }
        }
    }
}
