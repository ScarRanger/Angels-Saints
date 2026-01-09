package com.rhinepereira.angelsandsaints.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ContentApi {

    @GET("home.json")
    suspend fun getHome(
        @Header("Cache-Control") cacheControl: String? = null
    ): CategoryResponse

    @GET("categories/{categoryId}.json")
    suspend fun getCategoryItems(
        @Path("categoryId") categoryId: String,
        @Header("Cache-Control") cacheControl: String? = null
    ): CategoryItemsResponse

    @GET("items/{contentId}.json")
    suspend fun getContent(
        @Path("contentId") contentId: String,
        @Header("Cache-Control") cacheControl: String? = null
    ): ContentResponse
}
