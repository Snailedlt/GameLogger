package com.example.gamelogger.ui.mygamelist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelogger.classes.Game
import com.example.gamelogger.services.GameApi
import kotlinx.coroutines.launch

class MygamelistViewModel : ViewModel() {
    // The game list
    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>>
        get() = _games

    init {
        getGamesList()
    }

    private fun getGamesList() {
        viewModelScope.launch {
            _games.value = GameApi.retrofitService.getGameList("fromsoftware").results
            Log.d("ok", games.value.toString())
            Log.d("game0: ", games.value!![0].title)
        }
    }
}