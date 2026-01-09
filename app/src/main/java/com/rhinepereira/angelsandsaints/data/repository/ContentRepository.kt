package com.rhinepereira.angelsandsaints.data.repository

import com.rhinepereira.angelsandsaints.data.model.Category
import com.rhinepereira.angelsandsaints.data.model.Item
import com.rhinepereira.angelsandsaints.data.remote.ContentResponse
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    fun getCategories(): Flow<List<Category>>
    fun getItemsByCategory(categoryId: String): Flow<List<Item>>
    fun getItemById(itemId: String): Flow<Item?>
    fun getContent(contentId: String): Flow<ContentResponse>
}
