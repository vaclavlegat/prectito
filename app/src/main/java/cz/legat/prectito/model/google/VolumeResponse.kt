package cz.legat.prectito.model.google

import com.squareup.moshi.Json

data class VolumeResponse(
    @Json(name = "items") val items: List<Volume>
)