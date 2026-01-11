package com.rhinepereira.angelsandsaints.content

import com.rhinepereira.angelsandsaints.data.model.UiError
import com.rhinepereira.angelsandsaints.data.remote.DailyReadingsResponse
import java.util.Date

data class DailyReadingsState(
    val isLoading: Boolean = false,
    val date: Date = Date(),
    val data: DailyReadingsResponse? = null,
    val error: UiError? = null
)
