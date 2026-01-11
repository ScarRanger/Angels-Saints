package com.rhinepereira.saints.category

import com.rhinepereira.saints.data.model.Item
import com.rhinepereira.saints.data.model.UiError

data class CategoryState(
    val isLoading: Boolean = false,
    val categoryTitle: String = "Loading...",
    val items: List<Item> = emptyList(),
    val error: UiError? = null
)
