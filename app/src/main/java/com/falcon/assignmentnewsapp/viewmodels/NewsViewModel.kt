package com.falcon.assignmentnewsapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falcon.assignmentnewsapp.Resource
import com.falcon.assignmentnewsapp.Utils.API_KEY
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
        fetchNewsAndDisplay()
    }

    private fun fetchNewsAndDisplay() {
        viewModelScope.launch {
            try {
                repository.fetchTopHeadlines("in", API_KEY)
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Error fetching news", e)
            }
            _articles.value = Resource.Loading
            try {
                val result = repository.getNewsFromDB()
                _articles.value = Resource.Success(result)
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Error fetching news", e)
            }
        }

    }
}