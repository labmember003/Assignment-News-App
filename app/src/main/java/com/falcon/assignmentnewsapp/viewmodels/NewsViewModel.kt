package com.falcon.assignmentnewsapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falcon.assignmentnewsapp.di.NewsRepository
import com.falcon.assignmentnewsapp.modeels.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// NewsViewModel.kt
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    val articles: StateFlow<List<Article>> = repository.articles
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun refreshNews() {
        viewModelScope.launch {
            try {
                repository.fetchTopHeadlines("in", "cd5d106340b64feea1eb5e0eeaa8e700")
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Error fetching news", e)
            }
        }
    }
}
