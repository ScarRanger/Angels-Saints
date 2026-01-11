package com.rhinepereira.saints.home

import com.rhinepereira.saints.data.model.Category
import com.rhinepereira.saints.data.model.UiError

data class HomeState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: UiError? = null
)
