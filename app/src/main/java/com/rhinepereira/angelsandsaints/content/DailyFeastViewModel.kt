package com.rhinepereira.angelsandsaints.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhinepereira.angelsandsaints.data.model.UiError
import com.rhinepereira.angelsandsaints.data.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.Calendar
import java.util.Date

class DailyFeastViewModel(
    private val repository: ContentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyFeastState())
    val uiState: StateFlow<DailyFeastState> = _uiState.asStateFlow()

    init {
        loadDailyFeast(Date())
    }

    fun nextDay() {
        val calendar = Calendar.getInstance()
        calendar.time = _uiState.value.date
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        loadDailyFeast(calendar.time)
    }

    fun previousDay() {
        val calendar = Calendar.getInstance()
        calendar.time = _uiState.value.date
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        loadDailyFeast(calendar.time)
    }

    fun retry() {
        loadDailyFeast(_uiState.value.date)
    }

    private fun loadDailyFeast(date: Date) {
        viewModelScope.launch {
            repository.getDailyFeast(date)
                .onStart {
                    _uiState.update { it.copy(isLoading = true, error = null, date = date) }
                }
                .catch { e ->
                    val uiError = when (e) {
                        is IOException -> UiError.Network
                        is HttpException -> if (e.code() == 404) UiError.NotFound else UiError.Unknown(e.message())
                        else -> UiError.Unknown(e.message)
                    }
                    _uiState.update { it.copy(isLoading = false, error = uiError) }
                }
                .collect { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            data = response,
                            error = null
                        )
                    }
                }
        }
    }
}
