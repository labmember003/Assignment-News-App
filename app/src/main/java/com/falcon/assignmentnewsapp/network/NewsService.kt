package com.falcon.assignmentnewsapp.network

import com.falcon.assignmentnewsapp.Utils.API_KEY
import com.falcon.assignmentnewsapp.modeels.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "in",
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse
}

