package com.nik.capko.memo.ui.games.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nik.capko.memo.base.ui.BaseItemViewHolder
import com.nik.capko.memo.data.Game
import com.nik.capko.memo.databinding.ItemGameListBinding
import kotlin.properties.Delegates

class GamesAdapter(
    val onItemClick: (Int) -> Unit,
) : RecyclerView.Adapter<BaseItemViewHolder<ItemGameListBinding, Game>>() {

    var games: List<Game>? by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder<ItemGameListBinding, Game> {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemGameListBinding.inflate(inflater, parent, false)
        return ItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseItemViewHolder<ItemGameListBinding, Game>, position: Int) {
        games?.getOrNull(position)?.let { holder.onBind(it) }
    }

    override fun getItemCount(): Int = games?.size ?: 0

    inner class ItemViewHolder(itemBinding: ItemGameListBinding) : BaseItemViewHolder<ItemGameListBinding, Game>(itemBinding) {

        init {
            itemView.setOnClickListener { onItemClick.invoke(absoluteAdapterPosition) }
        }

        override fun onBind(item: Game) {
            itemBinding.apply {
                tvGameName.text = item.name
            }
        }
    }
}
