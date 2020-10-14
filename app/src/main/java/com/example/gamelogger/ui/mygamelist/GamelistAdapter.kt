/*
package com.example.gamelogger.ui.mygamelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelogger.databinding.GamelistItemCardBinding
import com.example.gamelogger.models.Game

class GamelistAdapter()
    : ListAdapter<Game, GamelistAdapter.GameViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(GamelistItemCardBinding.inflate(
            LayoutInflater.from(parent.context)
        ))
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
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

    class GameViewHolder(private var binding: GamelistItemCardBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(game: Game) {
            binding.game = game
            binding.executePendingBindings()
        }
    }

}*/

package com.example.profilside.ui.mygamelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelogger.R
import com.example.gamelogger.models.Game
import kotlinx.android.synthetic.main.gamelist_item_card.view.*

class GamelistAdapter(private var gamelist: ArrayList<Game>)
    : RecyclerView.Adapter<GamelistAdapter.ListViewHolder>() {
    private val gameList: ArrayList<Game> = gamelist

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val gameView = LayoutInflater.from(parent.context).inflate(
            R.layout.gamelist_item_card, parent, false
        )
        return ListViewHolder(gameView, this)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.itemView.apply {
            game_title.text = gameList[position].title
            game_platform.text = gameList[position].platform
            game_releasedate.text = gameList[position].releaseYear.toString()
        }
    }

    override fun getItemCount(): Int {
        return gamelist.size
    }

    inner class ListViewHolder(itemView: View, adapter: GamelistAdapter)
        : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView
        val platformView: TextView
        val releaseyearView: TextView
        val gameAdapter: GamelistAdapter

        init {
            titleView = itemView.findViewById(R.id.game_title)
            platformView = itemView.findViewById(R.id.game_platform)
            releaseyearView = itemView.findViewById(R.id.game_releasedate)
            gameAdapter = adapter
        }
    }
}