package cz.legat.core.model

import com.squareup.moshi.Json

data class Book(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "author") val author: Author? = null,
    @Json(name = "description") val description: String,
    @Json(name = "isbn") val isbn: String? = null,
    @Json(name = "published") val published: String? = null,
    @Json(name = "numberOfPages") val numberOfPages: String? = null,
    @Json(name = "imgLink") val imgLink: String,
    @Json(name = "genre") val genre: String? = null,
    @Json(name = "language") val language: String? = null,
    @Json(name = "translator") val translator: String? = null,
    @Json(name = "publisher") val publisher: String? = null,
    @Json(name = "rating") val rating: String? = null,
    @Json(name = "ratingsCount") val ratingsCount: String? = null
)