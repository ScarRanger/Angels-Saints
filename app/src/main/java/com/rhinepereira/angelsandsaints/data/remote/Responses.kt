package com.rhinepereira.angelsandsaints.data.remote

import com.rhinepereira.angelsandsaints.data.model.Category
import com.rhinepereira.angelsandsaints.data.model.Item
import com.rhinepereira.angelsandsaints.data.model.ContentBlock
import com.squareup.moshi.Json

data class CategoryResponse(
    @Json(name = "categories") val categories: List<Category>? = emptyList()
)

data class CategoryItemsResponse(
    @Json(name = "items") val items: List<Item>? = emptyList()
)

data class ContentResponse(
    @Json(name = "id") val id: String? = "",
    @Json(name = "title") val title: String? = "Untitled",
    @Json(name = "imageUrl") val imageUrl: String? = null,
    @Json(name = "blocks") val blocks: List<ContentBlock>? = emptyList()
)
