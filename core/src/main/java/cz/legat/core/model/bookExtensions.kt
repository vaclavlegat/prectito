package cz.legat.core.model

fun cz.legat.core.model.Book.bigImgLink(): String {
    val lastSlash = imgLink.lastIndexOf("/")
    val end = imgLink.substring(lastSlash+1)
    val start = imgLink.substring(0, lastSlash+1)
    return "${start}bmid_$end"
}

fun cz.legat.core.model.Book.middleImgLink(): String {
    val lastSlash = imgLink.lastIndexOf("/")
    val end = imgLink.substring(lastSlash+1)
    val start = imgLink.substring(0, lastSlash+1)
    return "${start}mid_$end"
}