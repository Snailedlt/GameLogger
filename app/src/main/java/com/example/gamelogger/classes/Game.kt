package com.example.gamelogger.classes

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class Game(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "name")
    val title: String,
    //val platform: String,
    val plattform: String? = "PS4",
    @Json(name = "released")
    var released: String?,
    @Json(name = "background_image")
    val img: String?,
    var state: GameState?
) : Parcelable {

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