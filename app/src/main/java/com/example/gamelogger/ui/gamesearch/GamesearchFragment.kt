package com.example.gamelogger.ui.gamesearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelogger.R
import com.example.gamelogger.models.Game
import com.example.profilside.ui.gamesearch.GamesearchViewModel
import com.example.profilside.ui.gamesearch.SearchListAdapter

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

//    private val viewModel: GamesearchViewModel by lazy {
//        ViewModelProvider(this).get(GamesearchViewModel::class.java)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val binding = FragmentGamesearchBinding.inflate(inflater)
//        return binding.root
//    }
}