package cz.legat.core.model

import com.squareup.moshi.Json

data class Book(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "author") val author: Author? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "isbn") val isbn: String? = null,
    @Json(name = "published") val published: String? = null,
    @Json(name = "numberOfPages") val numberOfPages: String? = null,
    @Json(name = "imgLink") val imgLink: String,
    @Json(name = "genre") val genre: String? = null,
    @Json(name = "language") val language: String? = null,
    @Json(name = "translator") val translator: String? = null,
    @Json(name = "publisher") val publisher: String? = null,
    @Json(name = "rating") val rating: String? = null,
    @Json(name = "ratingsCount") val ratingsCount: String? = null,
    @Json(name = "eBookLink") val eBookLink: String? = null
) : SearchResult {
    override fun getResultTitle(): String {
        return title
    }

    override fun getResultSubtitle(): String {
        if (description == null) {
            return ""
        }
        var author = ""
        var published = ""

        val split = description.split(",")
        if (split.size == 2) {
            author = split[1].trim()
            published = split[0].trim()
        } else {
            author = split[0]
        }
        return "$author - ($published)"
    }

    override fun getResultImgLink(): String {
        return imgLink
    }

    override fun getResultId(): String {
        return id
    }

    override fun isBook(): Boolean {
        return true
    }
}