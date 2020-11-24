package com.example.gamelogger.classes

import android.util.Log
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Game(
    //id is now required, because Int? doesn't work for safeargs from MygamelistFragment.kt to GamelistDetailFragment.kt
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    var title: String,
    //@Json(name = "platforms")
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
    @Json(name = "genres")
    var genre: Array<Genre>?,
    var genresFormatted: String?,
    var state: GameState?
) {

    init {
        this.state = GameState.BACKLOG
        this.released = this.releasedYear()
        Log.i("GenresFormatting", this.genreFormatting() + "")
        this.genresFormatted = this.genreFormatting()
    }

    private fun releasedYear() : String? {
        return if (!this.released.equals(null))
            this.released?.split("-")?.get(0)
        else
            null
    }

    private fun genreFormatting(): String {
        //Return formatted genre with comma between each genre
        var str ="N/A"
        if (!this.genre?.equals(null)!!){
            str = ""
            for(genre in this.genre!!){
                str += genre.genreName + ", "
            }
        }
        return str
    }

    /*private fun platforms(): String? {
        //Return formatted genre with comma between each genre
        return if (!this.genre.equals(null))
            this.genre?.split(",")?.get(0)
        else
            null
    }*/

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