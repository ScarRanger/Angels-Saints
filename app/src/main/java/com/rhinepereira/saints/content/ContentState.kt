package com.rhinepereira.saints.content

import com.rhinepereira.saints.data.remote.ContentResponse
import com.rhinepereira.saints.data.model.UiError

data class ContentState(
    val isLoading: Boolean = false,
    val content: ContentResponse? = null,
    val error: UiError? = null
)
