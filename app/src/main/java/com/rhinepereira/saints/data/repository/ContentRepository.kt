package com.rhinepereira.saints.data.repository

import com.rhinepereira.saints.data.model.Category
import com.rhinepereira.saints.data.model.Item
import com.rhinepereira.saints.data.remote.ContentResponse
import com.rhinepereira.saints.data.remote.DailyFeastResponse
import com.rhinepereira.saints.data.remote.DailyReadingsResponse
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ContentRepository {
    fun getCategories(): Flow<List<Category>>
    fun getItemsByCategory(categoryId: String): Flow<List<Item>>
    fun getItemById(itemId: String): Flow<Item?>
    fun getContent(contentId: String): Flow<ContentResponse>
    fun getDailyFeast(date: Date = Date()): Flow<DailyFeastResponse>
    fun getDailyReadings(date: Date = Date(), language: String = "english"): Flow<DailyReadingsResponse>
    suspend fun prefetchEverything()
}
