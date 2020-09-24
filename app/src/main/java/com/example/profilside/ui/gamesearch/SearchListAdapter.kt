package com.example.profilside.ui.gamesearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.profilside.R
import com.example.profilside.ui.mygamelist.Game
import com.example.profilside.ui.mygamelist.GamelistAdapter
import kotlinx.android.synthetic.main.gamelist_item_card.view.*

class SearchListAdapter(private var gamelist: ArrayList<Game>)
    : RecyclerView.Adapter<SearchListAdapter.ListViewHolder>() {
        private val gameList: ArrayList<Game> = gamelist

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
            val gameView = LayoutInflater.from(parent.context).inflate(
                R.layout.searchgamelist_item_card, parent, false
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

        inner class ListViewHolder(itemView: View, adapter: SearchListAdapter)
            : RecyclerView.ViewHolder(itemView) {
            val titleView: TextView
            val platformView: TextView
            val releaseyearView: TextView
            val gameAdapter: SearchListAdapter

            init {
                titleView = itemView.findViewById(R.id.game_title)
                platformView = itemView.findViewById(R.id.game_platform)
                releaseyearView = itemView.findViewById(R.id.game_releasedate)
                gameAdapter = adapter
            }
        }
}
