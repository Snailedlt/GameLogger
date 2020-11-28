package com.example.gamelogger.classes

import android.util.Log
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Class for the Game object to be used throughout the app
 */
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
    var dateAdded: String?
) {

    init {
        this.platformsList = this.platformsToStringArray()
        this.state = GameState.BACKLOG
        this.released = this.releaseDateToYear()
        this.genresList = this.genresToPlatformArray()
    }

    /**
     * Converts the game's release date to just hold the year
     */
    private fun releaseDateToYear(): String? {
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

    /**
     * Saves a specific platform to a Game object
     */
    fun setPlatform(platform: String?) {
        this.chosenPlatform = platform
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (platforms != null) {
            if (other.platforms == null) return false
            if (!platforms.contentEquals(other.platforms)) return false
        } else if (other.platforms != null) return false
        if (genres != null) {
            if (other.genres == null) return false
            if (!genres.contentEquals(other.genres)) return false
        } else if (other.genres != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = platforms?.contentHashCode() ?: 0
        result = 31 * result + (genres?.contentHashCode() ?: 0)
        return result
    }
}

/**
 * Object for the search results retrieved when doing searches to
 * the game database api. The results attribute stores a list of the
 * Game objects to be used for display.
 */
data class GameSearchResults(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Game>
)

/**
 * The Genre class
 */
data class Genre(
    var name: String?,
)

/**
 * The Platforms class
 */
data class Platforms(
    @Json(name = "platform")
    var platform: Platform?,
    @Json(name = "released_at")
    var released: String?,
)

/**
 * The Platform class
 */
data class Platform(
    var name: String?,
)

enum class GameState {
    DONE, PLAYING, BACKLOG
}