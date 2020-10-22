package com.example.gamelogger.ui.mygamelist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelogger.classes.Game
import com.example.gamelogger.classes.GameState
import com.example.gamelogger.services.GameApi
import com.example.gamelogger.services.getUserGames
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MygamelistViewModel : ViewModel() {
    // The game list
    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>>
        get() = _games

    /**
     * [_status] tells if the data in the fragment is loading, done loading
     * or if there was an error
     */
    private val _status = MutableLiveData<ListStatus>()
    val status: LiveData<ListStatus>
        get() = _status

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
                        for (id in savedGames) {
                            gamelist.add(GameApi.retrofitService.getMyGames(id))
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

    fun changeGameState(game: Game) {
        when (game.state) {
            GameState.DONE -> game.state = GameState.BACKLOG
            GameState.BACKLOG -> game.state = GameState.PLAYING
            GameState.PLAYING -> game.state = GameState.DONE
        }
    }
}

enum class ListStatus { LOADING, ERROR, EMPTY, DONE }