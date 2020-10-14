package com.example.gamelogger.services

import com.example.gamelogger.models.Game
import retrofit2.http.GET
import retrofit2.Call

interface GameService {

    @GET("games")
    fun getGames () : Call<Game>
}