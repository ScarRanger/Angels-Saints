package com.rhinepereira.angelsandsaints.category

import com.rhinepereira.angelsandsaints.data.model.Item
import com.rhinepereira.angelsandsaints.data.model.UiError

data class CategoryState(
    val isLoading: Boolean = false,
    val categoryTitle: String = "Loading...",
    val items: List<Item> = emptyList(),
    val error: UiError? = null
)
