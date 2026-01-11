package com.rhinepereira.angelsandsaints.data.repository

import com.rhinepereira.angelsandsaints.data.model.Category
import com.rhinepereira.angelsandsaints.data.model.Item
import com.rhinepereira.angelsandsaints.data.remote.ContentResponse
import com.rhinepereira.angelsandsaints.data.remote.DailyFeastResponse
import com.rhinepereira.angelsandsaints.data.remote.DailyReadingsResponse
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ContentRepository {
    fun getCategories(): Flow<List<Category>>
    fun getItemsByCategory(categoryId: String): Flow<List<Item>>
    fun getItemById(itemId: String): Flow<Item?>
    fun getContent(contentId: String): Flow<ContentResponse>
    fun getDailyFeast(date: Date = Date()): Flow<DailyFeastResponse>
    fun getDailyReadings(date: Date = Date()): Flow<DailyReadingsResponse>
    suspend fun prefetchEverything()
}
