package com.nik.capko.memo.ui.games.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nik.capko.memo.data.Game
import com.nik.capko.memo.databinding.ItemGameListBinding
import kotlin.properties.Delegates

class GamesAdapter(val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<GamesAdapter.ItemViewHolder>() {

    var games: List<Game>? by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemBinding =
            ItemGameListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(games?.getOrNull(position))
    }

    override fun getItemCount(): Int = games?.size ?: 0

    inner class ItemViewHolder(private val itemBinding: ItemGameListBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemView.setOnClickListener { onItemClick.invoke(absoluteAdapterPosition) }
        }

        fun bind(game: Game?) {
            game?.let {
                itemBinding.tvGameName.text = it.name
            }
        }
    }
}
