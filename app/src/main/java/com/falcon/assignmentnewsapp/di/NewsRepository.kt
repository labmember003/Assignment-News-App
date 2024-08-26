package com.falcon.assignmentnewsapp.di

import android.text.Html
import android.util.Log
import androidx.core.text.parseAsHtml
import com.falcon.assignmentnewsapp.modeels.Article
import com.falcon.assignmentnewsapp.network.DataFromContentService
import com.falcon.assignmentnewsapp.network.NewsService
import com.falcon.assignmentnewsapp.room.ArticleDao
import org.htmlparser.Parser
import org.htmlparser.nodes.TextNode
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

//    fun extractTextWithHtmlCleaner(htmlContent: String): String {
//        val parser = Parser(htmlContent)
//        val nodeList = parser.parse(null)
//        return nodeList
//    }
    fun extractTextWithStreamApi(htmlContent: String): String {
        return Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY).toString()
    }

    fun fetchWebPageContent(url: String, onResult: (String) -> Unit) {
        val webService = dataFromContentService
        val call = webService.getPageContent(url)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(extractTextWithStreamApi(it))
                    }
//                    Jsoup.parse(htmlContent)
//                    response.body()
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