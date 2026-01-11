package com.rhinepereira.saints.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhinepereira.saints.data.model.UiError
import com.rhinepereira.saints.data.remote.ContentResponse
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

class ContentViewModel(
    private val repository: ContentRepository,
    val categoryId: String,
    private val contentId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContentState())
    val uiState: StateFlow<ContentState> = _uiState.asStateFlow()

    init {
        loadContent()
    }

    fun retry() {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch {
            val isDailyFeast = categoryId.trim().lowercase() == "daily-feast"
            
            val contentFlow = if (isDailyFeast) {
                repository.getDailyFeast()
            } else {
                repository.getContent(contentId)
            }

            contentFlow
                .onStart { 
                    _uiState.update { it.copy(isLoading = true, error = null) } 
                }
                .catch { e ->
                    val uiError = mapError(e)
                    _uiState.update { it.copy(isLoading = false, error = uiError) }
                }
                .collect { response ->
                    val finalResponse = when (response) {
                        is com.rhinepereira.saints.data.remote.DailyFeastResponse -> {
                            ContentResponse(
                                id = "daily-feast",
                                title = response.observance?.title ?: "Daily Feast",
                                blocks = response.blocks
                            )
                        }
                        is ContentResponse -> response
                        else -> null
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            content = finalResponse,
                            error = null
                        )
                    }
                }
        }
    }

    private fun mapError(e: Throwable): UiError {
        return when (e) {
            is IOException -> UiError.Network
            is HttpException -> if (e.code() == 404) UiError.NotFound else UiError.Unknown(e.message())
            else -> UiError.Unknown(e.message)
        }
    }
}
