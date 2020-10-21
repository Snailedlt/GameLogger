package com.example.gamelogger.ui.gamesearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelogger.classes.Game
import com.example.gamelogger.services.GameApi
import com.example.gamelogger.services.addSavedGame
import kotlinx.coroutines.launch

class GamesearchViewModel : ViewModel() {

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

    private val searchstring : String = "final-fantasy"

    init {
        getGamesList(searchstring)
    }

    /**
     * Metode som kobler opp mot spilldatabasen og henter data
     */
    private fun getGamesList(searchstring: String) {
        viewModelScope.launch {
            _gamesearchresults.value = GameApi.retrofitService.getGameList(searchstring).results
            if (_gamesearchresults.value != null) {
                Log.d("ok", gamesearchresults.value.toString())
                gamesearchresults.value!![0].title?.let { Log.d("game0: ", it) }
            }
        }
    }

    /**
     * Adds the selected game to the [_savedgames] arraylist.
     * Should add the game's id along with the chosen platform to a user file that is uploaded
     * to a server instead of this method.
     */
    fun saveGame(game: Game) {
        savedgames.value?.add(game)
        game.gameadded = true
        Log.i("Saved game: ", "${game.title} with id ${game.id}")
        //searchGame("demon's souls")
        addSavedGame(game.id.toString(), "Playing")

    }

    fun searchGame(searchstring: String) {
        val string = searchstring
        string.replace(" ", "-")
        getGamesList(string)
    }
}