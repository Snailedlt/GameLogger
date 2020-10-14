package com.example.gamelogger.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Game(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val title: String,
    val platform: String,
    @Json(name = "Released")
    val released: Int,
    @Json(name = "background_image")
    val img: String,
    val state: GameState
) {

    companion object {
        fun makeGameList(): ArrayList<Game> {
            val gameList = ArrayList<Game>()
            for (i in 0..15) {
                gameList.add(Game(
                    2324,
                    "Game1",
                    "PS4",
                    2020,
                    "hehe",
                    GameState.PLAYING
                ))
            }
            return gameList
        }
    }
}

enum class GameState {
    DONE, PLAYING, BACKLOG
}