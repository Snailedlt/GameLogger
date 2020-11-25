package com.example.gamelogger.helpers

fun <T> listToPresentableString(list: List<T>?) : String {
    var string = ""
    if (list.isNullOrEmpty())
        return ""
    val listIterator = list.iterator()
    while (listIterator.hasNext()) {
        string += listIterator.next()
        string += ", "
    }
    return string
}