package com.falcon.assignmentnewsapp.modeels

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("totalResults")
    val totalResults: Int? = null,
    @SerializedName("articles")
    val articles: List<Article>? = null
)

data class Article(
    @SerializedName("source") val source: String? = null,
    @SerializedName("author") val author: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("url") val url: String?,
    @SerializedName("urlToImage") val urlToImage: String? = null,
    @SerializedName("publishedAt") val publishedAt: String? = null,
    @SerializedName("content") val content : String? = null
)
