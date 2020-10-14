package com.example.gamelogger.ui.mygamelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelogger.models.Game
import com.example.gamelogger.models.GameResults
import com.example.gamelogger.models.ResponseData
import com.example.gamelogger.services.GameApi
import com.example.gamelogger.services.GameData
import kotlinx.coroutines.launch

class MygamelistViewModel : ViewModel() {
    // The game list
    private val _games = MutableLiveData<List<Game>>()
    val games: MutableLiveData<List<Game>>
        get() = _games

    init {
        getGamesList()
    }

    private fun getGamesList() {
        viewModelScope.launch {
            _games.value = GameApi.retrofitService.getGameList("fromsoftware")
        }
    }
}