package com.example.gamelogger.ui.gamesearch

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelogger.R
import com.example.gamelogger.classes.Game
import com.example.gamelogger.databinding.FragmentGamesearchBinding
import com.example.gamelogger.services.deleteSavedGame
import com.google.android.material.snackbar.Snackbar


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
            if (it.platformsList?.size!! > 1)
                platformChoiceDialogue(requireView(), it)
            else if (it.platformsList!![0].isNullOrEmpty())
                viewModel.saveGame(it)
            else {
                it.setPlatform(it.platformsList!![0])
                viewModel.saveGame(it)
                showSnackBar(it)
                it.setPlatform(null)
            }
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
        val platforms = game.platformsList?.toTypedArray()
        Log.i("dialoguefunction", "clicked")
        Log.i("gameclicked: ", game.platforms.toString())
        // Log.i("gameclicked: ", game.platforms.platform)

        val builder = this.context?.let { AlertDialog.Builder(it) }
        with(builder)
        {
            Log.i("dialoguefunction", "inside with")
            this?.setTitle("Pick a platform for ${game.title}")
            this?.setItems(platforms) { dialog, which ->
                platforms?.get(which)?.let { game.setPlatform(it) }
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
                    it.findViewById(R.id.gamesearchlistLayout),
                    "${game.title} added to your list",
                    Snackbar.LENGTH_LONG)
                .setAction(
                    "UNDO",
                    View.OnClickListener {
                        deleteSavedGame(game.id.toString())
                        Log.i(game.title, "deleted")
                        // viewModel.deleteGame(game.id)
                    }
                )
        }
        val params: FrameLayout.LayoutParams = snackbar?.view?.layoutParams as FrameLayout.LayoutParams
        params.bottomMargin = 100
        snackbar.show()
    }

}