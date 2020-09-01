package cz.legat.prectito.model

fun Book.bigImgLink(): String {
    val lastSlash = imgLink.lastIndexOf("/")
    val end = imgLink.substring(lastSlash+1)
    val start = imgLink.substring(0, lastSlash+1)
    return "${start}bmid_$end"
}