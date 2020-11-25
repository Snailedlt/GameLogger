package com.example.gamelogger.ui.gamesearch

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelogger.R
import com.example.gamelogger.classes.Game
import com.example.gamelogger.databinding.FragmentGamesearchBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_gamelist_detail.*


class GamesearchFragment : Fragment() {

    /**
     * Initializes the [GamesearchFragment]
     */
    private val viewModel: GamesearchViewModel by lazy {
        ViewModelProvider(this).get(GamesearchViewModel::class.java)
    }

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGamesearchBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.gamesearchlist.adapter = SearchListAdapter(SearchListAdapter.OnClickListener {
            //viewModel.saveGame(it)
//            if (it.platforms.platform.size() > 1)
//                platformChoiceDialogue(requireView(), it)
//            else {
//                it.setPlatform(it.platforms.platform.name)
//                viewModel.saveGame(it)
//                showSnackBar(it)
//            }
            platformChoiceDialogue(requireView(), it)
        })

        /**
         * Search bar listener
         */
        searchView = binding.root.findViewById(R.id.searchBar)
        searchView.setOnClickListener { searchView.isIconified = false }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(arg0: String?): Boolean {
                val query = searchView.query.toString()
                viewModel.searchGame(query)
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
                    //Toast.makeText(getActivity()?.getApplicationContext(), "Getting More Results", Toast.LENGTH_SHORT).show()
                    /*viewModel.searchNextGame(query)*/
                }
            }
        })

        return binding.root
    }

    private fun platformChoiceDialogue(view: View, game: Game) {
        val platforms = arrayOf("PS4", "XBOX", "Nintendo Switch")
        Log.i("dialoguefunction", "clicked")

        val builder = this.context?.let { AlertDialog.Builder(it) }
        with(builder)
        {
            Log.i("dialoguefunction", "inside with")
            this?.setTitle("Pick a platform")
            this?.setItems(platforms) { dialog, which ->
                game.setPlatform(platforms[which])
                viewModel.saveGame(game)
                showSnackBar(game)
            }
            this?.show()
        }
    }

    // Show snackbar when adding a game
    private fun showSnackBar(game: Game) {
        val snackbar = view?.let {
            Snackbar
                .make(
                    it, "${game.title} added to your list", Snackbar.LENGTH_LONG)
                .setAction(
                    "UNDO",
                    View.OnClickListener {
                        Log.i("snackbar undo", "clicked")
                        // viewModel.deleteGame(game.id)
                    }
                )
        }
        snackbar?.show()
    }

}