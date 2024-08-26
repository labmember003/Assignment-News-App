package com.falcon.assignmentnewsapp.di

import android.util.Log
import com.falcon.assignmentnewsapp.network.DataFromContentService
import com.falcon.assignmentnewsapp.network.NewsService
import com.falcon.assignmentnewsapp.room.ArticleDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

@Singleton
class NewsRepository @Inject constructor(
    private val newsService: NewsService,
    private val articleDao: ArticleDao,
    private val dataFromContentService: DataFromContentService
) {
    suspend fun fetchTopHeadlines(country: String, apiKey: String) {
        val response = newsService.getTopHeadlines(country, apiKey)
        if (response.status == "ok") {
            articleDao.insertAll(response.articles?: emptyList())
            Log.i("NewsRepository", "${response.articles?.size} in db storeed")
        }
    }
    suspend fun fetchWebPageContent(url: String, onResult: (String) -> Unit) {
        val webService = dataFromContentService
        val call = webService.getPageContent(url)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    response.body()?.let { onResult(it) }
                } else {
                    Log.e("FetchContent", "Failed to fetch content: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("FetchContent", "Error fetching content", t)
            }
        })
    }
}