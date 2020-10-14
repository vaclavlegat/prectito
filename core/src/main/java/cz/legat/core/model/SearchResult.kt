package cz.legat.core.model

interface SearchResult {
    fun getResultTitle(): String
    fun getResultSubtitle(): String
    fun getResultImgLink(): String
    fun getResultId():String
    fun isBook():Boolean
}