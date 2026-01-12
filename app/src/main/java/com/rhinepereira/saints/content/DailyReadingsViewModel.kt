package com.rhinepereira.saints.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhinepereira.saints.data.model.UiError
import com.rhinepereira.saints.data.repository.ContentRepository
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

class DailyReadingsViewModel(
    private val repository: ContentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyReadingsState())
    val uiState: StateFlow<DailyReadingsState> = _uiState.asStateFlow()

    init {
        loadReadings(Date(), "English")
    }

    fun nextDay() {
        val calendar = Calendar.getInstance()
        calendar.time = _uiState.value.date
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        loadReadings(calendar.time, _uiState.value.language)
    }

    fun previousDay() {
        val calendar = Calendar.getInstance()
        calendar.time = _uiState.value.date
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        loadReadings(calendar.time, _uiState.value.language)
    }

    fun setLanguage(language: String) {
        if (_uiState.value.language != language) {
            loadReadings(_uiState.value.date, language)
        }
    }

    fun retry() {
        loadReadings(_uiState.value.date, _uiState.value.language)
    }

    private fun loadReadings(date: Date, language: String) {
        viewModelScope.launch {
            repository.getDailyReadings(date, language)
                .onStart {
                    _uiState.update { it.copy(isLoading = true, error = null, date = date, language = language) }
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
