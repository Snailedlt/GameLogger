package com.example.gamelogger.ui.gamesearch

import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelogger.classes.Game
import com.example.gamelogger.services.GameApi
import kotlinx.coroutines.launch

class GamesearchViewModel : ViewModel() {

    private val _gamesearchresults = MutableLiveData<List<Game>>()
    val gamesearchresults: LiveData<List<Game>>
        get() = _gamesearchresults

    private val _savedgames = MutableLiveData<ArrayList<Game>>()
    val savedgames: LiveData<ArrayList<Game>>
        get() = _savedgames

    init {
        getGamesList()
    }

    private fun getGamesList() {
        viewModelScope.launch {
            _gamesearchresults.value = GameApi.retrofitService.getGameList("square-enix").results
            Log.d("ok", gamesearchresults.value.toString())
            Log.d("game0: ", gamesearchresults.value!![0].title)
        }
    }

    fun saveGame(game: Game) {
        savedgames.value?.add(game)
        Log.i("Saved game: ", "${game.title} with id ${game.id}" )
    }
}