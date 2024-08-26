package com.falcon.assignmentnewsapp.di

import android.util.Log
import com.falcon.assignmentnewsapp.modeels.Article
import com.falcon.assignmentnewsapp.network.DataFromContentService
import com.falcon.assignmentnewsapp.network.NewsService
import com.falcon.assignmentnewsapp.room.ArticleDao
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsService: NewsService,
    private val articleDao: ArticleDao,
    private val dataFromContentService: DataFromContentService
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
    fun fetchWebPageContent(url: String, onResult: (String) -> Unit) {
        val webService = dataFromContentService
        val call = webService.getPageContent(url)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    response.body()?.let { onResult(it) }
//                    response.body()?.let { htmlContent ->
//                        val textContent = parseHtmlToText(htmlContent)
//                        onResult(textContent)
//                    }
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
fun parseHtmlToText(html: String): String {
    return try {
        // Use Jsoup to parse the HTML and extract text
        val document = Jsoup.parse(html)
        // Select the main content element - this will vary based on the site's HTML structure
        document.select("article").text() // Adjust the selector based on the structure
    } catch (e: IOException) {
        e.printStackTrace()
        "Error parsing HTML"
    }
}