package com.rhinepereira.angelsandsaints.content

import com.rhinepereira.angelsandsaints.data.model.UiError
import com.rhinepereira.angelsandsaints.data.remote.DailyFeastResponse
import java.util.Date

data class DailyFeastState(
    val isLoading: Boolean = false,
    val date: Date = Date(),
    val data: DailyFeastResponse? = null,
    val error: UiError? = null
)
