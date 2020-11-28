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
    // Retrieve a list of games when typing in search queries
    // This uses the api's own built-in search implementation
    @GET("games")
    suspend fun getGameList(@Query("search") type: String) : GameSearchResults

    // Retrieve a specific game using the game's database id
    @GET("games/{id}")
    suspend fun getGame(@Path("id") type: String) : Game
}

object GameApi {
    val retrofitService : GameApiService by lazy {
        retrofit.create(GameApiService::class.java)
    }
}
