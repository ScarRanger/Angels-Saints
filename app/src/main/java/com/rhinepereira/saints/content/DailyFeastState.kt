package com.rhinepereira.saints.content

import com.rhinepereira.saints.data.model.UiError
import com.rhinepereira.saints.data.remote.DailyFeastResponse
import java.util.Date

data class DailyFeastState(
    val isLoading: Boolean = false,
    val date: Date = Date(),
    val data: DailyFeastResponse? = null,
    val error: UiError? = null
)
