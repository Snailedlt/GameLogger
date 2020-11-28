package com.example.gamelogger.ui.gamelistdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelogger.Data.GameApi
import com.example.gamelogger.classes.Game
import com.example.gamelogger.services.getUserGames
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GamelistDetailViewModel(gameId: Int) : ViewModel() {
    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game>
        get() = _game

    init {
        //Log.i("GamelistDetailViewModel", "GamelistDetailViewModel created!")
        getGame(gameId)
    }

    override fun onCleared() {
        super.onCleared()
        //Log.i("GameViewModel", "GameViewModel destroyed!")
    }

    /**
     * Takes the gameId as an input, and assigns the gameObject to the private _game variable,
     * which can be accessed from the val game by external classes
     */
    fun getGame(gameId: Int) {

        try {
            getUserGames { currentGame ->
                CoroutineScope(viewModelScope.coroutineContext).launch {
                    val gameObject:Game = GameApi.retrofitService.getGame(gameId.toString())
                    //Log.i("GameInfo:", "Game: $gameObject")
                    _game.value = gameObject
                }
            }
        } catch (e: Exception) { Log.i("getGame Exception", "An exception occurred: $e")}
        //return GameApi.retrofitService.getMyGames(gameId.toString())
    }
}