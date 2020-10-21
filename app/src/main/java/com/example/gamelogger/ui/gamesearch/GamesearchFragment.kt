package com.example.gamelogger.ui.gamesearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gamelogger.R
import com.example.gamelogger.databinding.FragmentGamesearchBinding

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

        /**
         * Search bar listener
         */
        searchView = binding.root.findViewById(R.id.searchBar)
        searchView.setOnClickListener { searchView.isIconified = false }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(arg0: String?): Boolean {
                // filter listview
                Log.i("test1", "text submitted")
                return true
            }

            override fun onQueryTextChange(arg0: String?): Boolean {
                val query = searchView.query.toString()
                viewModel.searchGame(query)
                return false
            }
        })

        return binding.root
    }
}