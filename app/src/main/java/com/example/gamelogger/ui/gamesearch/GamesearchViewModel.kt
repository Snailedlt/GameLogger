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

    private val _gamesearchresult = MutableLiveData<GameSearchResults>()
    val gamesearchresult: LiveData<GameSearchResults>
        get() = _gamesearchresult

    /**
     * The search results
     */
    private val _gamesearchresults = MutableLiveData<List<Game>>()
    val gamesearchresults: LiveData<List<Game>>
        get() = _gamesearchresults

    /**
     * An arraylist that is added to whenever the function [saveGame] is called.
     * Should be reworked to add the game to an users list dynamically.
     */
    private val _savedgames = MutableLiveData<ArrayList<Game>>()
    val savedgames: LiveData<ArrayList<Game>>
        get() = _savedgames

    private val _status = MutableLiveData<SearchStatus>()
    val status: LiveData<SearchStatus>
        get() = _status

    private val _searchString = MutableLiveData<String>()
    val searchString: LiveData<String>
        get() = _searchString


    init {
        _status.value = SearchStatus.EMPTY
        //getGamesList(searchstring)
    }

    /**
     * Function that makes a connection to the API and populates the [_gamesearchresults]
     * mutable live data
     */
    private fun getGamesList() {
        viewModelScope.launch {
            _status.value = SearchStatus.LOADING
            try {
                _gamesearchresult.value = GameApi.retrofitService
                    .getGameList(searchString.value.toString())
                _gamesearchresults.value = gamesearchresult.value?.results
                if (searchString.value.toString() == "") {
                    _status.value = SearchStatus.EMPTY
                    _gamesearchresults.value = ArrayList()
                } else _status.value = SearchStatus.DONE
            } catch (e: Exception) {
                Log.i("Exception: ", e.toString())
                _status.value = SearchStatus.ERROR
                _gamesearchresults.value = ArrayList()
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
        savedgames.value?.add(game)
        Log.i("Saved game: ", "${game.title} with id ${game.id}, state is ${game.state.toString()}")
        addSavedGame(game.id.toString(), game.state.toString())
    }

    /**
     * Makes
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