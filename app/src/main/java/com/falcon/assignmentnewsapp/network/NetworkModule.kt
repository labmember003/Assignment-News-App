package com.falcon.assignmentnewsapp.network

import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNewsService(): NewsService {
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsService::class.java)
    }

    @Provides
    @Singleton
    fun provideDataFromContentService(): DataFromContentService {
        return Retrofit.Builder()
            .baseUrl("https://billi.mausi")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(DataFromContentService::class.java)
    }
}
