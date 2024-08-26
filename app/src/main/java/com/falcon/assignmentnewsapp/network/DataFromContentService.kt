package com.falcon.assignmentnewsapp.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface DataFromContentService {
    @GET
    fun getPageContent(@Url url: String): Call<String>
}
