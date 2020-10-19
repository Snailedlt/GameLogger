package com.example.gamelogger.ui.gamesearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gamelogger.R
import com.example.gamelogger.classes.Game
import com.example.gamelogger.databinding.FragmentGamesearchBinding
import com.example.gamelogger.ui.mygamelist.GamelistAdapter

class GamesearchFragment : Fragment() {

    /**
     * Initializes the [GamesearchFragment]
     */
    private val viewModel: GamesearchViewModel by lazy {
        ViewModelProvider(this).get(GamesearchViewModel::class.java)
    }

    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGamesearchBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.gamesearchlist.adapter = SearchListAdapter(SearchListAdapter.OnClickListener {
            viewModel.saveGame(it)
        })

        searchView = binding.root.findViewById(R.id.searchBar)
        searchView.setOnClickListener { searchView.isIconified = false }

        return binding.root
    }
}