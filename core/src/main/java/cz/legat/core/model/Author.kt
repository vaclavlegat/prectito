package cz.legat.core.model

import com.squareup.moshi.Json

data class Author(
    @Json(name = "authorId") val authorId: String? = null,
    @Json(name = "name") val name: String,
    @Json(name = "life") val life: String? = null,
    @Json(name = "authorImgLink") val authorImgLink: String? = null,
    @Json(name = "cv") val cv: String? = null,
    val books: List<Book>? = null
)