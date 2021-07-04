package com.nik.capko.memo.ui.dictionary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nik.capko.memo.data.Dictionary
import com.nik.capko.memo.databinding.ItemDictionaryListBinding
import kotlin.properties.Delegates

class DictionaryAdapter(val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<DictionaryAdapter.ItemViewHolder>() {

    var dictionaryList: List<Dictionary>? by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemBinding =
            ItemDictionaryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dictionaryList?.getOrNull(position))
    }

    override fun getItemCount(): Int = dictionaryList?.size ?: 0

    inner class ItemViewHolder(private val itemBinding: ItemDictionaryListBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemView.setOnClickListener { onItemClick.invoke(absoluteAdapterPosition) }
        }

        fun bind(dictionary: Dictionary?) {
            itemBinding.apply {
                dictionary?.let {
                    tvDictionaryName.text = it.name
                    tvDictionarySize.text = it.size.toString()
                }
            }
        }
    }
}
