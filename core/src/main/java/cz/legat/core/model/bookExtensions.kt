package cz.legat.core.model

fun Book.bigImgLink(): String {
    val lastSlash = imgLink.lastIndexOf("/")
    val end = imgLink.substring(lastSlash+1)
    val start = imgLink.substring(0, lastSlash+1)
    return "${start}bmid_$end"
}

fun Book.middleImgLink(): String {
    val lastSlash = imgLink.lastIndexOf("/")
    val end = imgLink.substring(lastSlash+1)
    val start = imgLink.substring(0, lastSlash+1)
    return "${start}mid_$end"
}