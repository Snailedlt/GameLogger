package com.example.gamelogger.helpers

import android.content.Context

/**
 * Converts a List to a string
 * To be used when displaying Game attribute fields such as
 * platformsList or genresList
 */
fun <T> listToPresentableString(list: List<T>?) : String {
    var string = ""
    if (list.isNullOrEmpty())
        return ""
    val listIterator = list.iterator()
    while (listIterator.hasNext()) {
        string += listIterator.next()
        string += ", "
    }
    return string.substring(0, string.length - 2)
}

/**
 * Takes a hashcode: Int as parameter,
 * and turns it into an Hexcode: String with the format RRGGBB
 */
fun intToRGB(i: Int): String? {
    return Integer.toHexString(i shr 16 and 0xFF) +
            Integer.toHexString(i shr 8 and 0xFF) +
            Integer.toHexString(i and 0xFF)
}