package com.rhinepereira.angelsandsaints.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhinepereira.angelsandsaints.data.model.UiError
import com.rhinepereira.angelsandsaints.data.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CategoryViewModel(
    private val repository: ContentRepository,
    private val categoryId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryState())
    val uiState: StateFlow<CategoryState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun retry() {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Fetch categories to find the title, and items for this category
            combine(
                repository.getCategories(),
                repository.getItemsByCategory(categoryId)
            ) { categories, items ->
                val categoryName = categories.find { it.id == categoryId }?.title ?: "Items"
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        categoryTitle = categoryName,
                        items = items,
                        error = null
                    )
                }
            }.catch { e ->
                val uiError = when (e) {
                    is IOException -> UiError.Network
                    is HttpException -> if (e.code() == 404) UiError.NotFound else UiError.Unknown(e.message())
                    else -> UiError.Unknown(e.message)
                }
                _uiState.update { it.copy(isLoading = false, error = uiError) }
            }.collect {}
        }
    }
}
