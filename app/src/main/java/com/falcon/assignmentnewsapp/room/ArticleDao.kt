package com.falcon.assignmentnewsapp.room

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.falcon.assignmentnewsapp.modeels.Article
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<Article>)

    @Query("SELECT * FROM article WHERE id = :id".toString())
    suspend fun insertCats(catCount: Int): Int {
        return catCount
    }

    @Query("SELECT * FROM article".toString())
    fun getAllArticles(): List<Article>
}

@Database(entities = [Article::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}


