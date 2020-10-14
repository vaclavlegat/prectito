package cz.legat.core.model

import com.squareup.moshi.Json

data class Author(
    @Json(name = "authorId") val authorId: String,
    @Json(name = "name") val name: String,
    @Json(name = "life") val life: String? = null,
    @Json(name = "authorImgLink") val authorImgLink: String,
    @Json(name = "cv") val cv: String? = null,
    val books: List<Book>? = null
): SearchResult{
    override fun getResultTitle(): String {
        return name
    }

    override fun getResultSubtitle(): String {
        return ""
    }

    override fun getResultImgLink(): String {
        return authorImgLink
    }

    override fun getResultId(): String {
        return authorId
    }

    override fun isBook(): Boolean {
        return false
    }
}