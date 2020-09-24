package com.example.profilside.ui.gamesearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.profilside.R
import com.example.profilside.ui.mygamelist.Game

class GamesearchFragment : Fragment() {

    private lateinit var gamesearchViewModel: GamesearchViewModel
    private lateinit var linLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        gamesearchViewModel =
//                ViewModelProviders.of(this).get(GamesearchViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_gamesearch, container, false)
//        val textView: TextView = root.findViewById(R.id.text_gamesearch)
//        gamesearchViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
//        return root
        val rootView = inflater.inflate(R.layout.fragment_gamesearch, container, false)
        val rvGamesearchList = rootView.findViewById(R.id.gamesearchlist) as RecyclerView
        linLayoutManager = LinearLayoutManager(context)
        rvGamesearchList.adapter = SearchListAdapter(Game.makeGameList())
        rvGamesearchList.layoutManager = linLayoutManager
        return rootView
    }
}