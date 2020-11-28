package com.example.gamelogger.ui.mygamelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelogger.databinding.GamelistItemCardBinding
import com.example.gamelogger.classes.Game
import com.example.gamelogger.classes.GameState

/**
 * Adapter class for the My Games list interface's recyclerview
 */
class GamelistAdapter(
    private val buttonClickListener: GameButtonListener,
    private val cardClickListener: GameCardListener
):
    ListAdapter<Game, GamelistAdapter.GameViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(getItem(position)!!, buttonClickListener, cardClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(GamelistItemCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.state == newItem.state
        }
    }

    class GameViewHolder constructor(val binding: GamelistItemCardBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Game, buttonClickListener: GameButtonListener, cardClickListener: GameCardListener) {
            binding.game = item
            binding.buttonClickListener = buttonClickListener
            binding.cardClickListener = cardClickListener
            binding.executePendingBindings()
        }
    }
}

/**
 * Clicklistener for the buttons to change a game's state
 */
class GameButtonListener(val clickListener: (game: Game, state: GameState) -> Unit) {
    fun myOnClick(game: Game, state: GameState) = clickListener(game, state)
}

/**
 * Clicklistener for the cards containing game information
 */
class GameCardListener(val clickListener: (game: Game) -> Unit) {
    fun cardOnClick(game: Game) = clickListener(game)
}