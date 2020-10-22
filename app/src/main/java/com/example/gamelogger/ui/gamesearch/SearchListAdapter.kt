package com.example.gamelogger.ui.gamesearch

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelogger.R
import com.example.gamelogger.classes.Game
import com.example.gamelogger.databinding.FragmentSearchItemCardBinding

class SearchListAdapter(private val onClickListener: OnClickListener)
    : ListAdapter<Game, SearchListAdapter.GamesearchViewHolder>(DiffCallback) {

    /**
     * Sørger for at fragmentet sitt view blir opprettet (inflated)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesearchViewHolder {
        return GamesearchViewHolder(FragmentSearchItemCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: GamesearchViewHolder, position: Int) {
        val game = getItem(position)
        val addButton = holder.itemView.findViewById<ImageButton>(R.id.add_button)
        addButton.setOnClickListener {
            onClickListener.onClick(game)
        }
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

    /**
     *
     */
    class GamesearchViewHolder(private var binding: FragmentSearchItemCardBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(game: Game) {
            binding.game = game
            binding.executePendingBindings()
        }
    }

    /**
     * onclick-objekt for add game-button
     */
    class OnClickListener(val clickListener: (game: Game) -> Unit) {
        fun onClick(game: Game) = clickListener(game)
    }
}