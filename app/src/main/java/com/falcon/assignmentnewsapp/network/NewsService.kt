package com.falcon.assignmentnewsapp.network

import com.falcon.assignmentnewsapp.modeels.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

// NewsService.kt
interface NewsService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "in",
        @Query("apiKey") apiKey: String = "cd5d106340b64feea1eb5e0eeaa8e700"
    ): NewsResponse
}
