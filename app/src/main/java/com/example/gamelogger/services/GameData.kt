package com.example.gamelogger.services

import com.squareup.moshi.Json

data class GameData<T>(
    @Json(name = "count")
    val count: Int,
    @Json(name = "next")
    val next: String,
    @Json(name = "previous")
    var prev: String,
    @Json(name = "results")
    var result: T
)