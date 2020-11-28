package com.example.gamelogger.ui.gamelistdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * This class is responsible for instantiating the GamelistDetailViewModel object.
 */
class GamelistDetailViewModelFactory(private val gameId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GamelistDetailViewModel::class.java)) {
            return GamelistDetailViewModel(gameId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}