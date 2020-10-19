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
            _games.value = GameApi.retrofitService.getGameList("Souls").results
            if (_games.value != null) {
                Log.d("ok", games.value.toString())
                games.value!![0].title?.let { Log.d("game0: ", it) }
            }
        }
    }
}