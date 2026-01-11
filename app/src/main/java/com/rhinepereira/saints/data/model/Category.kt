package com.rhinepereira.saints.data.model

import com.squareup.moshi.Json

data class Category(
    @Json(name = "id") val id: String = "",
    @Json(name = "title") val title: String = "Unknown",
    @Json(name = "description") val description: String? = null,
    @Json(name = "imageUrl") val imageUrl: String = ""
)
