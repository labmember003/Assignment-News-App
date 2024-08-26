package com.falcon.assignmentnewsapp.di

import android.util.Log
import com.falcon.assignmentnewsapp.modeels.Article
import com.falcon.assignmentnewsapp.network.NewsService
import com.falcon.assignmentnewsapp.room.ArticleDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsService: NewsService,
    private val articleDao: ArticleDao,
) {
    suspend fun getNewsFromDB(): List<Article> {
        return articleDao.getAllArticles()
    }
    suspend fun fetchTopHeadlines(country: String, apiKey: String) {
        val response = newsService.getTopHeadlines(country, apiKey)
        if (response.status == "ok") {
            articleDao.insertAll(response.articles?: emptyList())
            Log.i("NewsRepository", "${response.articles?.size} in db storeed")
        }
    }
}