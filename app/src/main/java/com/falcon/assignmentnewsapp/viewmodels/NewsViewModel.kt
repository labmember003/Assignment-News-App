package com.falcon.assignmentnewsapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falcon.assignmentnewsapp.Resource
import com.falcon.assignmentnewsapp.di.NewsRepository
import com.falcon.assignmentnewsapp.modeels.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _articles = MutableStateFlow<Resource<List<Article>>>(Resource.Loading)
    val articles: StateFlow<Resource<List<Article>>> get() = _articles

    init {
        fetchNews()
        displayNews()
    }

    fun fetchNews() {
        viewModelScope.launch {
            try {
                repository.fetchTopHeadlines("in", "cd5d106340b64feea1eb5e0eeaa8e700")
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Error fetching news", e)
            }
        }
    }

    private fun displayNews() {
        _articles.value = Resource.Loading
        viewModelScope.launch {
            try {
                val result = repository.getNewsFromDB()
                _articles.value = Resource.Success(result)
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Error fetching news", e)
            }
        }
    }

    fun getDataFromUrl(url: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            repository.fetchWebPageContent(url, onResult)
        }
    }
}
