package com.example.gamelogger.classes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Genre(

    @Json(name = "id")
    var genreId: Int?,
    @Json(name = "name")
    var genreName: String?,
    var genresFormatted: String?,
    @Json(name = "slug")
    var genreSlug: String?,
)