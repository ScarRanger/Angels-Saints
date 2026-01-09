package com.rhinepereira.angelsandsaints.home

import com.rhinepereira.angelsandsaints.data.model.Category
import com.rhinepereira.angelsandsaints.data.model.UiError

data class HomeState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: UiError? = null
)
