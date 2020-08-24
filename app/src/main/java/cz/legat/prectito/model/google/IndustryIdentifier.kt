package cz.legat.prectito.model.google

import com.squareup.moshi.Json

data class IndustryIdentifier(
    @Json(name = "type") val type: String,
    @Json(name = "identifier") val identifier: String
)