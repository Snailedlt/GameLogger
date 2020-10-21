package com.example.gamelogger.ui.gamesearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var recyclerView: RecyclerView

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


        recyclerView = binding.root.findViewById(R.id.gamesearchlist) as RecyclerView
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val query = searchView.query.toString()
                if (!recyclerView.canScrollVertically(1)) {
                    Toast.makeText(getActivity()?.getApplicationContext(), "Getting More Results", Toast.LENGTH_SHORT).show()
                    viewModel.searchNextGame(query)
                }
            }
        })

        return binding.root
    }
}