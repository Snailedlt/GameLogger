package com.example.gamelogger.ui.mygamelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelogger.databinding.GamelistItemCardBinding
import com.example.gamelogger.classes.Game
import com.example.gamelogger.classes.GameState

class GamelistAdapter(val clickListener: GameButtonListener):
    ListAdapter<Game, GamelistAdapter.GameViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {

        // clicklisteners for game card buttons, for changing game state
        //val game = getItem(position)
//        val doneButton = holder.itemView.findViewById<Button>(R.id.button_done)
//        val playingButton = holder.itemView.findViewById<Button>(R.id.button_playing)
//        val backlogButton = holder.itemView.findViewById<Button>(R.id.button_planning)
//
//        doneButton.setOnClickListener {
//            onClickListener.onClick(game)
//        }
//        playingButton.setOnClickListener {
//            onClickListener.onClick(game)
//        }
//        backlogButton.setOnClickListener {
//            onClickListener.onClick(game)
//        }
        holder.bind(getItem(position)!!, clickListener)
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
        fun bind(item: Game, clickListener: GameButtonListener) {
            binding.game = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }
}

class GameButtonListener(val clickListener: (game: Game, state: GameState) -> Unit) {
    fun myOnClick(game: Game, state: GameState) = clickListener(game, state)
}