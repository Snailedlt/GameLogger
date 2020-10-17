package com.example.gamelogger.ui.gamesearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelogger.classes.Game
import com.example.gamelogger.databinding.FragmentSearchItemCardBinding

class SearchListAdapter()
    : ListAdapter<Game, SearchListAdapter.GamesearchViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesearchViewHolder {
        return GamesearchViewHolder(FragmentSearchItemCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: GamesearchViewHolder, position: Int) {
        val game = getItem(position)
        holder.bind(game)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class GamesearchViewHolder(private var binding: FragmentSearchItemCardBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(game: Game) {
            binding.game = game
            binding.executePendingBindings()
        }
    }
}