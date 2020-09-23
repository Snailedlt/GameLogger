package com.example.profilside.ui.mygamelist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.profilside.R
import kotlinx.android.synthetic.main.gamelist_item_card.view.*

class GamelistAdapter(private val myContext: Context, private var gamelist: ArrayList<Game>)
    : RecyclerView.Adapter<GamelistAdapter.ListViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(myContext)
    private val gameList: ArrayList<Game> = gamelist

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val gameView = mInflater.inflate(
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