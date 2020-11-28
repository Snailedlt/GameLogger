package com.example.gamelogger.ui.mygamelist

import android.util.Log
import android.widget.Spinner
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelogger.classes.Game
import com.example.gamelogger.classes.GameState
import com.example.gamelogger.Data.GameApi
import com.example.gamelogger.services.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

/**
 * ViewModel class for the user's list of games
 */
class MygamelistViewModel : ViewModel() {

    // The LiveData list of games to be presented
    private val _games = MutableLiveData<MutableList<Game>>()
    val games: LiveData<MutableList<Game>>
        get() = _games

    // The game most recently being interacted with, primarily used to restore a deleted game
    private val _currentgame = MutableLiveData<Game>()
    val currentgame: LiveData<Game>
        get() = _currentgame

    // status to keep track of if the app data is empty, loading or encountered an error
    private val _status = MutableLiveData<ListStatus>()
    val status: LiveData<ListStatus>
        get() = _status

    //
    private val _navigateToGameListDetail = MutableLiveData<Game>()
    val navigateToGameListDetail
        get() = _navigateToGameListDetail

    init {
        _status.value = ListStatus.LOADING
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
                // Gets users stored games in a MutableList that contains game id, state,
                // chosen platform and date added
                getUserGames { savedGames ->
                    CoroutineScope(viewModelScope.coroutineContext).launch {
                        val gamelist = ArrayList<Game>()
                        var count = 0
                        var state = GameState.BACKLOG
                        var chosenPlatform = "_"
                        var dateAdded = "_"
                        _status.value = ListStatus.LOADING
                        // Loops all info from getUserGames function
                        for (id in savedGames) {
                            // Only picks up numbers which is id
                            if (id.isDigitsOnly()){
                                //
                                gamelist.add(GameApi.retrofitService.getGame(id))
                                // Adds data into correct Game object
                                gamelist[count].state = state
                                gamelist[count].chosenPlatform = chosenPlatform
                                gamelist[count].dateAdded = dateAdded
                                count++
                            }
                            // Only picks up data with am and pm endings which are dates
                            else if (id.endsWith("am") || id.endsWith("pm")) {
                                dateAdded = id
                            }
                            else {
                                // Only picks up gamestates
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
                                    // Rest are platforms
                                    else -> {
                                        chosenPlatform = id
                                    }
                                }
                            }
                        }
                        _games.value = gamelist
                        _status.value = ListStatus.DONE
                        Log.i("Liststatus:", status.value.toString())
                        if (!games.value.isNullOrEmpty()) {
                            Log.i("getgames value: ", games.value.toString())
                            Log.i("getgames size: ", games.value!!.size.toString())
                            _status.value = ListStatus.DONE
                        } else {
                            _status.value = ListStatus.EMPTY
                        }
                    }
                }
            } catch (e: Exception) { Log.i("h", "h")}
            Log.i("status: ", status.value.toString())
        }
    }

    /**
     * Function to change a game's state.
     * Called when pressing one of the three buttons in the gamelist view
     * to determine if you're done playing, currently playing or planning to play (backlog)
     * a game.
     */
    fun changeGameState(game: Game, state: GameState) {
        _currentgame.value = game
        viewModelScope.launch {
            game.state = state
            changeDatabaseGameState(game.id.toString(), state.toString())
        }
    }

    /**
     * Removes the game at the specified position, as well as from the database
     * It also copies that game to [_currentgame] so it can easily be restored again
     * when the user wants to undo deletion
     */
    fun removeGame(position: Int) {
        val game = games.value?.get(position)
        _currentgame.value = game?.copy()
        _currentgame.value?.state = game?.state // ensures that the correct state is restored if the user undos the deletion
        _currentgame.value?.dateAdded = game?.dateAdded
        viewModelScope.launch {
            deleteSavedGame(game?.id.toString())
        }
        _games.value?.removeAt(position)
        if (games.value.isNullOrEmpty()) // checks if list is empty after deletion
            _status.value = ListStatus.EMPTY
    }

    /**
     * Undos deletion of a game
     */
    fun undoRemoveGame(position: Int) {
        val game = currentgame.value
        viewModelScope.launch {
            game?.chosenPlatform?.let { it1 ->
                addSavedGame(game)
            }
        }
        game?.let { _games.value?.add(position, it) }
        _status.value = ListStatus.DONE
        _currentgame.value = null
    }

    fun onGamelistDetailNavigated() {
        _navigateToGameListDetail.value = null
    }

    fun onGamelistDetailClicked(game: Game) {
        _navigateToGameListDetail.value = game
    }

    /**
     *   Funksjon som sorterer My List gjennom valg fra en spinner,
     *   Kan sortere ved navn, utgivelse dato eller dato spillet ble lagt til i databasen,
     *   Sorterer listen og deretter sletter den usorterte og setter inn den sorterte
     */
    fun sortMyGamesList(sortSpinner: Spinner, gamelist:MutableList<Game>){
        val game = games.value
        if (game != null) {
            if (game.isNotEmpty()) {
                val sortedList: MutableList<Game>
                when(sortSpinner.selectedItem.toString()) {
                    "Name" -> {
                        sortedList = gamelist.sortedBy{ it.title } as MutableList<Game>
                        _games.value?.removeAll(sortedList)
                        _games.value?.addAll(sortedList)
                    }
                    "Release date" -> {
                        sortedList = gamelist.sortedBy{ it.released } as MutableList<Game>
                        _games.value?.removeAll(sortedList)
                        _games.value?.addAll(sortedList)
                    }
                    "Date added" -> {
                        sortedList = gamelist.sortedByDescending{ it.dateAdded } as MutableList<Game>
                        _games.value?.removeAll(sortedList)
                        _games.value?.addAll(sortedList)
                    }
                }
            }
        }


    }

}

// enum class that defines the app data status
enum class ListStatus { LOADING, ERROR, EMPTY, DONE }