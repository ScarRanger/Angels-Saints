package com.rhinepereira.angelsandsaints.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhinepereira.angelsandsaints.data.model.UiError
import com.rhinepereira.angelsandsaints.data.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getContent(contentId)
                .catch { e ->
                    val uiError = when (e) {
                        is IOException -> UiError.Network
                        is HttpException -> if (e.code() == 404) UiError.NotFound else UiError.Unknown(e.message())
                        else -> UiError.Unknown(e.message)
                    }
                    _uiState.update { it.copy(isLoading = false, error = uiError) }
                }
                .collect { contentResponse ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            content = contentResponse,
                            error = null
                        ) 
                    }
                }
        }
    }
}
