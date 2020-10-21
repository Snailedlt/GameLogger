package com.example.gamelogger.ui.mygamelist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.coroutineScope
import com.example.gamelogger.classes.Game
import com.example.gamelogger.services.GameApi
import com.example.gamelogger.services.addSavedGame
import com.example.gamelogger.services.getUserGames
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
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
        viewModelScope.launch {
            try {
                getUserGames { savedGames ->
                    CoroutineScope(viewModelScope.coroutineContext).launch {
                        Log.i("savedgameslength", "${savedGames.size}")
                        val gamelist = ArrayList<Game>()
                        for (id in savedGames) {
                            Log.i("idLog", id)
                            gamelist.add(GameApi.retrofitService.getMyGames(id))
                            Log.i("Gamesvalue", gamelist.toString())
                        }
                        _games.value = gamelist
                    }
                }
            } catch (e: Exception) { Log.i("h", "h")}
            if (_games.value != null) {
                _status.value = ListStatus.DONE
            } else _status.value = ListStatus.EMPTY
        }
    }
}

enum class ListStatus { LOADING, ERROR, EMPTY, DONE }