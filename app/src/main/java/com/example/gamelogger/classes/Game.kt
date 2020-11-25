package com.example.gamelogger.classes

import android.util.Log
import com.example.gamelogger.services.addSavedGame
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Game(
    //id is now required, because Int? doesn't work for safeargs from MygamelistFragment.kt to GamelistDetailFragment.kt
    val id: Int,
    @Json(name = "name")
    var title: String,
    //@Json(name = "platforms")
    var platforms: Array<Platforms>?,
    var platformsList: List<String?>?,
    var chosenPlatform: String?,
    var released: String?,
    @Json(name = "background_image")
    val img: String?,
    @Json(name = "description")
    val about: String?,
    val metacritic: Int?,
    var genres: Array<Genre>?,
    var genresList: List<String?>?,
    var state: GameState?,
) {

    init {
        Log.i("GameInfoPlatformsArray", platforms.toString() + "")
        this.platformsList = this.platformsToStringArray()
        Log.i("GameInfoPlatformsString", "After: " + platformsList.toString())
        this.state = GameState.BACKLOG
        this.released = this.releasedYear()
        this.genresList = this.genresToPlatformArray()
    }

    private fun releasedYear(): String? {
        return if (!this.released.equals(null))
            this.released?.split("-")?.get(0)
        else
            null
    }

    private fun genresToPlatformArray(): List<String?>? {
        return if (!this.genres.isNullOrEmpty()) {
            val list = mutableListOf<String?>()
            for (i in this.genres!!.indices) {
                list.add(this.genres!![i].name)
            }
            list.toList()
        } else null
    }

    private fun platformsToStringArray(): List<String?>? {
        return if (!this.platforms.isNullOrEmpty()){
            val list = mutableListOf<String?>()
            for (i in this.platforms!!.indices) {
                this.platforms!![i].platform?.name?.let { Log.i("PlatformstoString${i}", it) }
                list.add(this.platforms!![i].platform?.name)
            }
            list.toList()
        } else null
    }

   private fun compareFirebasePlatformToAPIPlatform(): String? {
        //return platforms that are in Firebase and the API call for a single game
        return null
    }

    fun setPlatform(platform: String) {
        this.chosenPlatform = platform
    }
}

data class GameSearchResults(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Game>
)

data class Genre(
    var name: String?,
)

data class Platforms(
    @Json(name = "platform")
    var platform: Platform?,
    @Json(name = "released_at")
    var released: String?,
)

data class Platform(
    var name: String?,
)

enum class GameState {
    DONE, PLAYING, BACKLOG
}