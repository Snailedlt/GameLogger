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
    private val _lastInteractedGame = MutableLiveData<Game>()
    val lastInteractedGame: LiveData<Game>
        get() = _lastInteractedGame

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
                                // Uses the id to get the game from the api and adds it to the list
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
            } catch (e: Exception) {
                _status.value = ListStatus.ERROR
            }
        }
    }

    /**
     * Function to change a game's state.
     * Called when pressing one of the three buttons in the gamelist view
     * to determine if you're done playing, currently playing or planning to play (backlog)
     * a game.
     */
    fun changeGameState(game: Game, state: GameState) {
        viewModelScope.launch {
            game.state = state
            changeDatabaseGameState(game.id.toString(), state.toString())
        }
    }

    /**
     * Removes the game at the specified position, as well as from the database
     * It also copies that game to [_lastInteractedGame] so it can easily be restored again
     * when the user wants to undo deletion
     */
    fun removeGame(position: Int) {
        val game = games.value?.get(position)
        _lastInteractedGame.value = game?.copy() // copies the game to lastInteractedGame so that action can be undone
        _lastInteractedGame.value?.state = game?.state // ensures that the correct state is restored if the user undos the deletion
        _lastInteractedGame.value?.dateAdded = game?.dateAdded // ensures that the correct date is restored if the user undos the deletion
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
        val game = lastInteractedGame.value // gets the game to be readded
        viewModelScope.launch {
            game?.chosenPlatform?.let { addSavedGame(game) }
        }
        game?.let { _games.value?.add(position, it) }

        // If you only have one game in your list and you remove it, status will be set to EMPTY.
        // This code will make sure that when undoing this (removing the last game), status will
        // be DONE again to not display the EMPTY message to the user
        _status.value = ListStatus.DONE

        _lastInteractedGame.value = null // cleans up the value
    }

    fun onGamelistDetailNavigated() {
        _navigateToGameListDetail.value = null
    }

    fun onGamelistDetailClicked(game: Game) {
        _navigateToGameListDetail.value = game
    }

    /**
     *   Function that sorts My List
     */
    fun sortMyGamesList(sortSpinner: Spinner, gamelist:MutableList<Game>){
        val game = games.value
        if (game != null) {
            if (game.isNotEmpty()) {
                val sortedList: MutableList<Game>
                when(sortSpinner.selectedItem.toString()) {
                    "Name" -> {
                        // Sorts the list by name alphabetically
                        sortedList = gamelist.sortedBy{ it.title } as MutableList<Game>
                        // Removes the the old list
                        _games.value?.removeAll(sortedList)
                        // Adds the new sorted list
                        _games.value?.addAll(sortedList)
                    }
                    "Release date" -> {
                        // Sorts the list by release date
                        sortedList = gamelist.sortedBy{ it.released } as MutableList<Game>
                        // Removes the the old list
                        _games.value?.removeAll(sortedList)
                        // Adds the new sorted list
                        _games.value?.addAll(sortedList)
                    }
                    "Date added" -> {
                        // Sorts the list by name date added to list
                        sortedList = gamelist.sortedByDescending{ it.dateAdded } as MutableList<Game>
                        // Removes the the old list
                        _games.value?.removeAll(sortedList)
                        // Adds the new sorted list
                        _games.value?.addAll(sortedList)
                    }
                }
            }
        }
    }

}

// enum class that defines the app data status
enum class ListStatus { LOADING, ERROR, EMPTY, DONE }