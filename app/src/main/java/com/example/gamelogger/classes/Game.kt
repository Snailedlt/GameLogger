package com.example.gamelogger.classes

import android.util.Log
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
    var platformsString: String?,
    var plattform: String? = "PS4",
    var released: String?,
    @Json(name = "background_image")
    val img: String?,
    @Json(name = "description")
    val about: String?,
    val metacritic: Int?,
    var genres: Array<Genre>?,
    var genresString: String?,
    var state: GameState?,
) {

    init {
        //Log.i("GameInfoPlatformsArray", platforms?.joinToString()+ "")
        this.platformsString = this.platformArrayToString()
        Log.i("GameInfoPlatformsString", "After: " + platformsString)
        this.state = GameState.BACKLOG
        this.released = this.releasedYear()
        this.genresString = this.genreArrayToString()
    }

    private fun releasedYear() : String? {
        return if (!this.released.equals(null))
            this.released?.split("-")?.get(0)
        else
            null
    }

    private fun genreArrayToString(): String? {
        //Return formatted genre with comma between each genre
        return if (!this.genres.isNullOrEmpty()){
            var str =""
            for(genre in this.genres!!){
                str += genre.name + ", "
            }
            str.substring(0, str.length - 2) //Removes the comma and space at the end of the string
        } else null
    }

    private fun platformArrayToString(): String? {
        //Return formatted genre with comma between each genre
        return if (!this.platforms.isNullOrEmpty()){
            var str =""
            for(platformer in this.platforms!!){
                str += platformer.platform?.name + ", "
            }
            str.substring(0, str.length - 2) //Removes the comma and space at the end of the string
        } else null
    }

   private fun compareFirebasePlatformToAPIPlatform(): String? {
        //return platforms that are in Firebase and the API call for a single game
        return null
    }

    fun setPlatform(platform: String) {
        this.plattform = platform
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