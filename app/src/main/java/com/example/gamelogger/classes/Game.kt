package com.example.gamelogger.classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Game(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val title: String,
    //val platform: String,
    val plattform: String = "console",
    @Json(name = "Released")
    val released: String?,
    @Json(name = "background_image")
    val img: String,
    val state: GameState?
) {
    override fun toString(): String {
        return super.toString()
    }
}

data class GameSearchResults(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Game>
)

enum class GameState {
    DONE, PLAYING, BACKLOG
}