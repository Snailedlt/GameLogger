package com.example.gamelogger.classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Game(
    //id is now required, because Int? doesn't work for safeargs from MygamelistFragment.kt to GamelistDetailFragment.kt
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    var title: String,
    //val platform: String,
    val plattform: String? = "PS4",
    @Json(name = "released")
    var released: String?,
    @Json(name = "background_image")
    val img: String?,
    @Json(name = "description")
    val about: String?,
    @Json(name = "metacritic")
    val metascore: Int?,
    //@Json(name = "genres")
    //val genre: String?,
    var state: GameState?
) {

    init {
        this.state = GameState.BACKLOG
        this.released = this.releasedYear()
    }

    private fun releasedYear() : String? {
        return if (!this.released.equals(null))
            this.released?.split("-")?.get(0)
        else
            null
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