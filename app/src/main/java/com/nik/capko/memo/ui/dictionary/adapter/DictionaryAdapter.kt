package com.nik.capko.memo.ui.dictionary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nik.capko.memo.base.ui.BaseItemViewHolder
import com.nik.capko.memo.data.Dictionary
import com.nik.capko.memo.databinding.ItemDictionaryListBinding
import kotlin.properties.Delegates

class DictionaryAdapter(val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<BaseItemViewHolder<ItemDictionaryListBinding, Dictionary>>() {

    var dictionaryList: List<Dictionary>? by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder<ItemDictionaryListBinding, Dictionary> {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemDictionaryListBinding.inflate(inflater, parent, false)
        return ItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseItemViewHolder<ItemDictionaryListBinding, Dictionary>, position: Int) {
        dictionaryList?.getOrNull(position)?.let { holder.onBind(it) }
    }

    override fun getItemCount(): Int = dictionaryList?.size ?: 0

    inner class ItemViewHolder(itemBinding: ItemDictionaryListBinding) : BaseItemViewHolder<ItemDictionaryListBinding, Dictionary>(itemBinding) {

        init {
            itemView.setOnClickListener { onItemClick.invoke(absoluteAdapterPosition) }
        }

        override fun onBind(item: Dictionary) {
            itemBinding.apply {
                tvDictionaryName.text = item.name
                tvDictionarySize.text = item.size.toString()
            }
        }
    }
}
