package com.example.gamelogger.ui.mygamelist

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelogger.classes.Game
import com.example.gamelogger.classes.GameState
import com.example.gamelogger.Data.GameApi
import com.example.gamelogger.helpers.bindGameStateButtons1
import com.example.gamelogger.services.changeDatabaseGameState
import com.example.gamelogger.services.getUserGameState
import com.example.gamelogger.services.getUserGames
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MygamelistViewModel : ViewModel() {

    // The LiveData list of games to be presented
    val _games = MutableLiveData<MutableList<Game>>()
    val games: LiveData<MutableList<Game>>
        get() = _games


    // The game being interacted with
    private val _currentgame = MutableLiveData<Game>()
    val currentgame: LiveData<Game>
        get() = _currentgame

    /**
     * [_status] tells if the data in the fragment is loading, done loading
     * or if there was an error
     */
    private val _status = MutableLiveData<ListStatus>()
    val status: LiveData<ListStatus>
        get() = _status


    private val _navigateToGameListDetail = MutableLiveData<Game>()
    val navigateToGameListDetail
        get() = _navigateToGameListDetail

    init {
        getGamesList()
    }

    /**
     * Method that is called whenever the viewmodel is initialized.
     * It calls on [getUserGames] to contact the app database (firebase)
     * to get a list of the user's saved games as their id values.
     * The list is iterated through to create [Game] objects for each id, that will be
     * stored in [games] to display a list for the user.
     */
    private fun getGamesList() {
        viewModelScope.launch {
            try {
                getUserGames { savedGames ->
                    CoroutineScope(viewModelScope.coroutineContext).launch {
                        val gamelist = ArrayList<Game>()
                        var count = 0
                        var state = GameState.BACKLOG
                        var chosenPlatform = "HELPIMSTUCKINTHISPHONE"

                        for (id in savedGames) {
                            if (id.isDigitsOnly()){

                                gamelist.add(GameApi.retrofitService.getMyGames(id))
                                gamelist[count].state = state
                                gamelist[count].chosenPlatform = chosenPlatform
                                count++
                            } else {
                                when (id) {
                                    "BACKLOG" -> {
                                        state = GameState.BACKLOG
                                    }
                                    "PLAYING" -> {
                                        state = GameState.PLAYING
                                    }
                                    "DONE" -> {
                                        state = GameState.DONE
                                    }
                                    else -> {
                                        chosenPlatform = id
                                    }
                                }
                            }
                        }
                        _games.value = gamelist
                        _status.value = ListStatus.DONE
                        Log.i("Liststatus:", status.value.toString())
                    }
                }
            } catch (e: Exception) { Log.i("h", "h")}
            if (games.value != null) {
                _status.value = ListStatus.DONE
                Log.i("Liststatus:", status.value.toString())
            } else {
                _status.value = ListStatus.EMPTY
            }
        }
    }

    /**
     * Function to change
     */
    fun changeGameState(game: Game, state: GameState) {
        _currentgame.value = game
        viewModelScope.launch {
            game.state = state
            changeDatabaseGameState(game.id.toString(), state.toString())
        }
    }

    fun onGamelistDetailNavigated() {
        _navigateToGameListDetail.value = null
    }

    fun onGamelistDetailClicked(game: Game) {
        _navigateToGameListDetail.value = game
    }
}

enum class ListStatus { LOADING, ERROR, EMPTY, DONE }