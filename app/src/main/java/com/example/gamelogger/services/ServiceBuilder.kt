package com.example.gamelogger.services

import com.example.gamelogger.classes.Game
import com.example.gamelogger.classes.GameSearchResults
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// API URL
private const val URL = "https://api.rawg.io/api/"

// CREATE HTTP CLIENT
//private val okHttp = OkHttpClient.Builder()

// moshi builder
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Retrofit builder
//private val builder = Retrofit.Builder().baseUrl(URL)
//    .addConverterFactory(MoshiConverterFactory.create())
//    .client(okHttp.build())
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(URL)
    .build()

// Query to the game API
interface GameApiService {
    @GET("games")
    suspend fun getGameList(@Query("search") type: String):
            GameSearchResults

    @GET("games?{page}")
    suspend fun getNextPage(
        @Path("page") page: String,
        @Query("search") type: String
    )

    @GET("games/{id}")
    suspend fun getMyGames(@Path("id") type: String):
            Game
}

object GameApi {
    val retrofitService : GameApiService by lazy {
        retrofit.create(GameApiService::class.java)
    }
}
