package com.rhinepereira.angelsandsaints.data.model

import com.squareup.moshi.Json

data class ContentBlock(
    @Json(name = "type") val type: String? = "text",
    @Json(name = "value") val value: String? = ""
)
