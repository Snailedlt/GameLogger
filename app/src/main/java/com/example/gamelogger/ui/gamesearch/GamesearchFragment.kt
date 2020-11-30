package com.example.gamelogger.ui.gamesearch

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelogger.R
import com.example.gamelogger.classes.Game
import com.example.gamelogger.databinding.FragmentGamesearchBinding
import com.example.gamelogger.firebase.deleteSavedGame
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


/**
 * The Fragment class for the Game search interface
 */
class GamesearchFragment : Fragment() {

    // Initializes the [GamesearchFragment]
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

        // Gets the adapter for the recyclerview, as well as defines the
        // onclick methods for the recyclerview items
        binding.gamesearchlist.adapter = SearchListAdapter(SearchListAdapter.OnClickListener {
            // The following code in this block is for when the user clicks on the
            // add button on a game in the search list
            //Log.i("GameSearchClick", "before if")
            if(it.platformsList.isNullOrEmpty()) {
                //Log.i("GameSearchClick", "if")
                it.setPlatform("Unknown")
                viewModel.saveGame(it)
            }
            else if (it.platformsList?.size!! > 1) {// if the game is available on more than a single platform, call platFormChoiceDialogue
                //Log.i("GameSearchClick", "else if")
                platformChoiceDialogue(requireView(), it)
            }
            else { // if the game has one or zero platforms, save it directly
                //Log.i("GameSearchClick", "else")
                it.setPlatform(it.platformsList!![0])
                viewModel.saveGame(it)
                showSnackBar(it)
            }
            it.setPlatform(null) // removes the saved game from the game object, as the relevant data has been saved to the database and "it" doesn't need the value
        })

        // Search bar listener
        searchView = binding.root.findViewById(R.id.searchBar)
        searchView.setOnClickListener { searchView.isIconified = false }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(arg0: String?): Boolean {
                val query = searchView.query.toString()
                viewModel.searchGame(query)
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

    /**
     * Function to show an alert dialogue with a game's
     * available platforms, prompting the user to pick the platform
     * they want to save
     */
    private fun platformChoiceDialogue(view: View, game: Game) {
        val platforms = game.platformsList?.toTypedArray() // converts the List of platforms to an Array the AlertDialogue class can use to set items

        // Builds an AlertDialog
        val builder = this.context?.let { AlertDialog.Builder(it) }
        with(builder)
        {
            this?.setTitle("Pick a platform for ${game.title}")
            this?.setItems(platforms) { dialog, which ->
                // Populates the dialogue's list of choices,
                // as well as defining what to do when clicking them
                platforms?.get(which)?.let { game.setPlatform(it) }
                viewModel.saveGame(game)
                showSnackBar(game)
            }
            this?.show()
        }
    }

    /**
     * Shows snackbar when a game is added
     */
    private fun showSnackBar(game: Game) {
        val snackbar = view?.let {
            Snackbar
                .make(
                    it.findViewById(R.id.gamesearchlistLayout),
                    "${game.title} added to your list",
                    Snackbar.LENGTH_LONG)
                .setAction(
                    "UNDO",
                    // UNDO button to allow user to undo adding a game
                    View.OnClickListener {
                        deleteSavedGame(game.id.toString())
                    }
                )
        }
        // Code to make the snackbar not overlap the navigation bar
        val bottomNavigationView = this.view?.rootView?.findViewById<BottomNavigationView>(R.id.nav_view)
        snackbar?.anchorView = bottomNavigationView
        // Animates the snackbar with a fade in instead of sliding in from the bottom
        snackbar?.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE

        snackbar?.show()
    }

}