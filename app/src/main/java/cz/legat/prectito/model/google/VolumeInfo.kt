package cz.legat.prectito.model.google

import com.squareup.moshi.Json

data class VolumeInfo(
    var id: String? = null,
    @Json(name = "title") val title: String,
    @Json(name = "subtitle") val subtitle: String?,
    @Json(name = "authors") val authors: List<String>,
    @Json(name = "publishedDate") val publishedDate: String,
    @Json(name = "industryIdentifiers") val industryIdentifiers: List<IndustryIdentifier>,
    @Json(name = "pageCount") val pageCount: String,
    @Json(name = "language") val language: String
)