package com.example.gamelogger.ui.mygamelist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelogger.classes.Game
import com.example.gamelogger.services.GameApi
//import com.example.gamelogger.services.getUserGames
import kotlinx.coroutines.launch
import kotlin.reflect.jvm.internal.impl.util.ModuleVisibilityHelper

class MygamelistViewModel : ViewModel() {
    // The game list
    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>>
        get() = _games

    private val _status = MutableLiveData<ListStatus>()
    val status: LiveData<ListStatus>
        get() = _status

    init {
        getGamesList()
    }

    private fun getGamesList() {
        //getUserGames() { savedGames ->
        //   Log.d("yo", "Spill som brukeren har lagret i sin database PLOX: $savedGames")

        viewModelScope.launch {
            //_games.value = GameApi.retrofitService.getGameList("Souls").results
            if (_games.value != null) {
                _status.value = ListStatus.DONE
                Log.d("ok", games.value.toString())
                games.value!![0].title?.let { Log.d("game0: ", it) }
            } else _status.value = ListStatus.EMPTY
        }
    }
}

enum class ListStatus { LOADING, ERROR, EMPTY, DONE }