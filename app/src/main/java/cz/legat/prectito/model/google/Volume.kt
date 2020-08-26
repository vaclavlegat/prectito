package cz.legat.prectito.model.google

import com.squareup.moshi.Json

data class Volume(
    @Json(name = "id") val id: String,
    @Json(name = "volumeInfo") val volumeInfo: VolumeInfo
)