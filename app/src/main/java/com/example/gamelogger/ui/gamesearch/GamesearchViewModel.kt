package com.example.gamelogger.ui.gamesearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelogger.classes.Game
import com.example.gamelogger.classes.GameSearchResults
import com.example.gamelogger.classes.GameState
import com.example.gamelogger.Data.GameApi
import com.example.gamelogger.firebase.addSavedGame
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * The ViewModel class for the Game search interface
 */
class GamesearchViewModel : ViewModel() {

    // The GameSearchResults object retrieved from the api call
    private val _gamesearchresult = MutableLiveData<GameSearchResults>()
    val gamesearchresult: LiveData<GameSearchResults>
        get() = _gamesearchresult

    // The Mutable LiveData that contains the list of [Game]s the search returns
    private val _gameresultlist = MutableLiveData<List<Game>>()
    val gameresultlist: LiveData<List<Game>>
        get() = _gameresultlist

    // An arraylist that is added to whenever the function [saveGame] is called.
    // Should be reworked to add the game to an users list dynamically.
    private val _savedgames = MutableLiveData<ArrayList<Game>>()
    val savedgames: LiveData<ArrayList<Game>>
        get() = _savedgames

    // status to tell the bindingadapters methods if the data is loading, done loading or had an error
    private val _status = MutableLiveData<SearchStatus>()
    val status: LiveData<SearchStatus>
        get() = _status

    // The SearchString that is used when searching for games in [getGamesList]
    private val _searchString = MutableLiveData<String>()
    val searchString: LiveData<String>
        get() = _searchString

    init {
        _status.value = SearchStatus.EMPTY
    }

    /**
     * Function that makes a connection to the API and populates the [_gameresultlist]
     * mutable live data
     */
    private fun getGameSearchResults() {
        viewModelScope.launch {
            _status.value = SearchStatus.LOADING
            try {
                // makes a call to the API service and creates a GameSearchResults object of the results
                _gamesearchresult.value = GameApi.retrofitService
                    .getGameList(searchString.value.toString())
                // fills the list of games with the array of Game objects stored in the GameSearchResults object
                _gameresultlist.value = gamesearchresult.value?.results
                // the rest of the code mostly just sets the search status based on if there's still
                // text in the search field or if there was an error. It also makes sure to empty the
                // gameresultlist so that there aren't any search results displayed
                if (searchString.value.toString() == "") {
                    _status.value = SearchStatus.EMPTY
                    _gameresultlist.value = ArrayList()
                } else _status.value = SearchStatus.DONE
            } catch (e: Exception) {
                Log.i("Exception: ", e.toString())
                _status.value = SearchStatus.ERROR
                _gameresultlist.value = ArrayList()
            }
        }
    }

    /**
     * Method called when saving a game to your list
     */
    fun saveGame(game: Game) {
        game.state = GameState.BACKLOG // by default sets the game's state to BACKLOG (equivalent to "planning to play")
        val date = Date() // Gets the current date
        val formatter = SimpleDateFormat("dd MMM yyyy HH:mma", Locale.UK) // Formats the date
        val dateAdded: String = formatter.format(date)
        game.dateAdded = dateAdded
        addSavedGame(game) // saves game to the app's firebase db
        game.dateAdded = null
    }

    /**
     * Function that gets the search string from a search bar, updates the [_searchString]
     * value and then runs the [getGameSearchResults] function
     */
    fun searchGame(searchstring: String) {
        val string = searchstring
        string.replace(" ", "-")
        _searchString.value = string
        getGameSearchResults()
    }

}

enum class SearchStatus { LOADING, ERROR, EMPTY, DONE }