package com.example.gamelogger.classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Game(
    //id is now required, because Int? doesn't work for safeargs from MygamelistFragment.kt to GamelistDetailFragment.kt
    val id: Int,
    @Json(name = "name")
    var title: String,
    //@Json(name = "platforms")
    //var platforms: Array<Platforms>?,
    val plattform: String? = "PS4",
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
        this.state = GameState.BACKLOG
        this.released = this.releasedYear()
        this.genresString = this.arrayToString()
    }

    private fun releasedYear() : String? {
        return if (!this.released.equals(null))
            this.released?.split("-")?.get(0)
        else
            null
    }

    private fun arrayToString(): String? {
        //Return formatted genre with comma between each genre
        return if (!this.genres.isNullOrEmpty()){
            var str =""
            for(genre in this.genres!!){
                str += genre.name + ", "
            }
            str
        } else null
    }
}

data class GameSearchResults(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Game>
)

data class Genre(
    var id: Int?,
    var name: String?,
    var slug: String?,
)

/*data class Platforms(
    @Json(name = "platform")
    var platforms: Array<Platform>?,
    var platformsFormatted: String?,
    @Json(name = "released_at")
    var released: String?,
) {

    init {
        this.platformsFormatted = this.platformFormatting()
        Log.i("GameInfoPlatforms", platformsFormatted + "")
    }


    private fun platformFormatting(): String? {
        //Return formatted genre with comma between each genre
        return if (!this.platforms.isNullOrEmpty()){
            var str =""
            for(platform in this.platforms!!){
                str += platform.name + ", "
            }
            str
        } else null
    }
}

data class Platform(
    var id: Int?,
    var name: String?,
    var slug: String?,
)*/

enum class GameState {
    DONE, PLAYING, BACKLOG
}