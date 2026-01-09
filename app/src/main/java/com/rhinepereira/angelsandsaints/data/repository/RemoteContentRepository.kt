package com.rhinepereira.angelsandsaints.data.repository

import com.rhinepereira.angelsandsaints.data.model.Category
import com.rhinepereira.angelsandsaints.data.model.Item
import com.rhinepereira.angelsandsaints.data.remote.ContentApi
import com.rhinepereira.angelsandsaints.data.remote.ContentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteContentRepository(
    private val api: ContentApi
) : ContentRepository {

    override fun getCategories(): Flow<List<Category>> = flow {
        val response = api.getHome()
        emit(response.categories ?: emptyList())
    }

    override fun getItemsByCategory(categoryId: String): Flow<List<Item>> = flow {
        val response = api.getCategoryItems(categoryId)
        emit(response.items ?: emptyList())
    }

    override fun getItemById(itemId: String): Flow<Item?> = flow {
        emit(null) 
    }

    override fun getContent(contentId: String): Flow<ContentResponse> = flow {
        val response = api.getContent(contentId)
        emit(response)
    }
}
