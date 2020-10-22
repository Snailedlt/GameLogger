package com.example.gamelogger.ui.mygamelist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelogger.R
import com.example.gamelogger.databinding.GamelistItemCardBinding
import com.example.gamelogger.classes.Game

class GamelistAdapter(private val onClickListener: OnClickListener)
    : ListAdapter<Game, GamelistAdapter.GameViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(GamelistItemCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = getItem(position)
        val doneButton = holder.itemView.findViewById<Button>(R.id.button_done)
        val playingButton = holder.itemView.findViewById<Button>(R.id.button_playing)
        val backlogButton = holder.itemView.findViewById<Button>(R.id.button_planning)
        doneButton.setOnClickListener {
            onClickListener.onClick(game)
        }
        playingButton.setOnClickListener {
            onClickListener.onClick(game)
        }
        backlogButton.setOnClickListener {
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

    class GameViewHolder(private var binding: GamelistItemCardBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(game: Game) {
            binding.game = game
            binding.executePendingBindings()
        }
    }

    /**
     * onClick-objekt for change state-buttons
     */
    class OnClickListener(val clickListener: (game: Game) -> Unit) {
        fun onClick(game: Game) = clickListener(game)
    }
}