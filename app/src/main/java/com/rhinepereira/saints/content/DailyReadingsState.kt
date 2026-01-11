package com.rhinepereira.saints.content

import com.rhinepereira.saints.data.model.UiError
import com.rhinepereira.saints.data.remote.DailyReadingsResponse
import java.util.Date

data class DailyReadingsState(
    val isLoading: Boolean = false,
    val date: Date = Date(),
    val data: DailyReadingsResponse? = null,
    val error: UiError? = null
)
