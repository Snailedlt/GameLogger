package com.example.gamelogger.ui.gamesearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelogger.classes.Game
import com.example.gamelogger.classes.GameSearchResults
import com.example.gamelogger.classes.GameState
import com.example.gamelogger.services.GameApi
import com.example.gamelogger.services.addSavedGame
import kotlinx.coroutines.launch

class GamesearchViewModel : ViewModel() {

    /**
     * The Mutable LiveData that contains the search result as an object [GameSearchResults]
     */
    private val _gamesearchresult = MutableLiveData<GameSearchResults>()
    val gamesearchresult: LiveData<GameSearchResults>
        get() = _gamesearchresult

    /**
     * The Mutable LiveData that contains the list of [Game]s the search returns
     */
    private val _gameresultlist = MutableLiveData<List<Game>>()
    val gameresultlist: LiveData<List<Game>>
        get() = _gameresultlist

    /**
     * An arraylist that is added to whenever the function [saveGame] is called.
     * Should be reworked to add the game to an users list dynamically.
     */
    private val _savedgames = MutableLiveData<ArrayList<Game>>()
    val savedgames: LiveData<ArrayList<Game>>
        get() = _savedgames

    /**
     * [_status] tells if the data in the fragment is loading, done loading
     * or if there was an error
     */
    private val _status = MutableLiveData<SearchStatus>()
    val status: LiveData<SearchStatus>
        get() = _status

    /**
     * The SearchString that is used when searching for games in [getGamesList]
     */
    private val _searchString = MutableLiveData<String>()
    val searchString: LiveData<String>
        get() = _searchString

    init {
        _status.value = SearchStatus.EMPTY
        //getGamesList(searchstring)
    }

    /**
     * Function that makes a connection to the API and populates the [_gameresultlist]
     * mutable live data
     */
    private fun getGamesList() {
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

    /*private fun getNextGamesList() {
        viewModelScope.launch {
            _status.value = SearchStatus.LOADING
            try {
                _gamesearchresults.value = GameApi.retrofitService
                    .getGameList(getNextPageURL(searchString.value.toString())).results
                if (searchString.value.toString().equals("")) {
                    _status.value = SearchStatus.EMPTY
                    _gamesearchresults.value = ArrayList()
                } else _status.value = SearchStatus.DONE
            } catch (e: Exception) {
                Log.i("Exception: ", e.toString())
                _status.value = SearchStatus.ERROR
                _gamesearchresults.value = ArrayList()
            }
        }
    }*/

    /**
     * Adds the selected game to the [_savedgames] arraylist.
     * Should add the game's id along with the chosen platform to a user file that is uploaded
     * to a server instead of this method.
     */
    fun saveGame(game: Game) {
        game.state = GameState.BACKLOG
        Log.i("Saved game: ", "${game.title} with id ${game.id}, state is ${game.state.toString()}")
        addSavedGame(game.id.toString(), game.state.toString())
    }

    /**
     * Function that gets the search string from a search bar, updates the [_searchString]
     * value and then runs the [getGamesList] function
     */
    fun searchGame(searchstring: String) {
        Log.i("Searched: ", searchstring)
        val string = searchstring
        string.replace(" ", "-")
        _searchString.value = string
        getGamesList()
    }

    /*fun searchNextGame() {
        _gamesearchresult.value = GameApi.retrofitService.getNextPage(gamesearchresult.value?.next.toString())
    }*/

    private fun getNextPageURL(url: String): String {
        return url.substring(29)
    }
}

enum class SearchStatus { LOADING, ERROR, EMPTY, DONE }