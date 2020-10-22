package com.example.gamelogger.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamelogger.classes.Game

class SharedViewModel : ViewModel() {
    val games = MutableLiveData<List<Game>>()

}