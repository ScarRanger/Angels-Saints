package com.rhinepereira.angelsandsaints.content

import com.rhinepereira.angelsandsaints.data.remote.ContentResponse
import com.rhinepereira.angelsandsaints.data.model.UiError

data class ContentState(
    val isLoading: Boolean = false,
    val content: ContentResponse? = null,
    val error: UiError? = null
)
