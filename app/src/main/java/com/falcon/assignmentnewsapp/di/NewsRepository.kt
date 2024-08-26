package com.falcon.assignmentnewsapp.di

import com.falcon.assignmentnewsapp.modeels.Article
import com.falcon.assignmentnewsapp.network.NewsService
import com.falcon.assignmentnewsapp.room.ArticleDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsService: NewsService,
    private val articleDao: ArticleDao
) {
    val articles: Flow<List<Article>> = articleDao.getAllArticles()

    suspend fun fetchTopHeadlines(country: String, apiKey: String) {
        val response = newsService.getTopHeadlines(country, apiKey)
        if (response.status == "ok") {
            articleDao.insertAll(response.articles)
        }
    }
}
