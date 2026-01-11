package com.rhinepereira.angelsandsaints.home

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

class HomeViewModel(
    private val repository: ContentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        loadCategories()
        // Prefetch everything for a complete offline experience
        viewModelScope.launch {
            repository.prefetchEverything()
        }
    }

    fun retry() {
        loadCategories()
        viewModelScope.launch {
            repository.prefetchEverything()
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getCategories()
                .catch { e ->
                    val uiError = when (e) {
                        is IOException -> UiError.Network
                        is HttpException -> if (e.code() == 404) UiError.NotFound else UiError.Unknown(e.message())
                        else -> UiError.Unknown(e.message)
                    }
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            error = uiError
                        ) 
                    }
                }
                .collect { categories ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            categories = categories,
                            error = null
                        )
                    }
                }
        }
    }
}
