package com.rhinepereira.saints.data.repository

import android.util.Log
import com.rhinepereira.saints.data.model.Category
import com.rhinepereira.saints.data.model.Item
import com.rhinepereira.saints.data.remote.ContentApi
import com.rhinepereira.saints.data.remote.ContentResponse
import com.rhinepereira.saints.data.remote.DailyFeastResponse
import com.rhinepereira.saints.data.remote.DailyReadingsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RemoteContentRepository(
    private val api: ContentApi
) : ContentRepository {

    override fun getCategories(): Flow<List<Category>> = flow {
        val response = api.getHome()
        val categories = (response.categories ?: emptyList())
            .filter { it.enabled }
            .sortedBy { it.order }
        emit(categories)
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

    override fun getDailyFeast(date: Date): Flow<DailyFeastResponse> = flow {
        val year = SimpleDateFormat("yyyy", Locale.US).format(date)
        val month = SimpleDateFormat("MM", Locale.US).format(date)
        val fullDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date)
        val dynamicPath = "feast-daily/$year/$month/$fullDate.json"
        
        Log.d("DailyFeast", "Fetching from: $dynamicPath")
        
        val response = api.getDailyFeast(dynamicPath)
        emit(response)
    }

    override fun getDailyReadings(date: Date, language: String): Flow<DailyReadingsResponse> = flow {
        val year = SimpleDateFormat("yyyy", Locale.US).format(date)
        val month = SimpleDateFormat("MM", Locale.US).format(date)
        val fullDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date)
        
        val dynamicPath = if (language.lowercase() == "marathi") {
            // Marathi path: readings-marathi/YYYY/YYYY-MM-DD.json (No Month folder)
            "readings-marathi/$year/$month/$fullDate.json"
        } else {
            // English path: readings/YYYY/MM/YYYY-MM-DD.json
            "readings/$year/$month/$fullDate.json"
        }
        
        Log.d("DailyReadings", "Language: $language | Path: $dynamicPath")
        Log.d("DailyReadings", "Expected Full URL: https://scarranger.github.io/angelsandsaints_data/content/$dynamicPath")
        
        val response = api.getDailyReadings(dynamicPath)
        emit(response)
    }

    override suspend fun prefetchEverything() {
        coroutineScope {
            try {
                val home = api.getHome()
                home.categories?.forEach { category ->
                    launch {
                        try {
                            when (category.id.trim()) {
                                "daily-feast" -> getDailyFeast(Date()).collect {}
                                "daily-readings" -> {
                                    getDailyReadings(Date(), "english").collect {}
                                    getDailyReadings(Date(), "marathi").collect {}
                                }
                                else -> {
                                    val categoryItems = api.getCategoryItems(category.id)
                                    categoryItems.items?.forEach { item ->
                                        launch {
                                            try {
                                                api.getContent(item.id)
                                            } catch (e: Exception) {}
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {}
                    }
                }
            } catch (e: Exception) {}
        }
    }
}
