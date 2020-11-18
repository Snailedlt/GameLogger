package com.example.gamelogger.Data

import com.example.gamelogger.classes.Game
import com.example.gamelogger.classes.GameSearchResults
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

// API URL for rawg video game db
private const val URL = "https://api.rawg.io/api/"

// moshi builder
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Retrofit builder
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(URL)
    .build()

// Queries to the game API
interface GameApiService {
    @GET("games")
    suspend fun getGameList(@Query("search") type: String):
            GameSearchResults

    @GET("games/{id}")
    suspend fun getMyGames(@Path("id") type: String):
            Game

    @GET
    suspend fun getNextPage(@Url type: String):
            GameSearchResults
}

object GameApi {
    val retrofitService : GameApiService by lazy {
        retrofit.create(GameApiService::class.java)
    }
}
