package com.example.gamelogger.helpers

import android.content.Context

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
 * Uses the context, and a pixel value, to convert into a Density-independent Pixel(dp) value
 */
fun dpFromPx(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.density
}

/**
 * Uses the context, and a Density-independent Pixel(dp) value, to convert into a pixel value
 */
fun pxFromDp(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
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