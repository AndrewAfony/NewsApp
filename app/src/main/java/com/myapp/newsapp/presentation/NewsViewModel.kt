package com.myapp.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.newsapp.domain.model.Article
import com.myapp.newsapp.domain.model.NewsResponse
import com.myapp.newsapp.domain.repository.NewsRepository
import com.myapp.newsapp.util.Resource
import com.myapp.newsapp.util.toNormTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {

    var breakingNewsPage = 1
    var breakingNews = MutableStateFlow(NewsState())
        private set
    var breakingNewsResponse: NewsResponse? = null

    var searchNewsPage = 1
    var searchQuery = MutableStateFlow("")
        private set

    var searchNews = MutableStateFlow(NewsState())
        private set
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("ru")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
            repository.getBreakingNews(countryCode, breakingNewsPage)
                .onEach {result ->
                    when(result) {
                        is Resource.Success -> {

                            breakingNewsPage++
                            if(breakingNewsResponse == null) {
                                breakingNewsResponse = result.data
                            } else {
                                val oldArticles = breakingNewsResponse?.articles
                                val newArticles = result.data?.articles
                                if (newArticles != null) {
                                    oldArticles?.addAll(newArticles)
                                }
                            }

                            breakingNews.value = breakingNews.value.copy(
                                isLoading = false,
                                news = breakingNewsResponse
                            )
                        }
                        is Resource.Error -> {
                            breakingNews.value = breakingNews.value.copy(
                                isLoading = false,
                                news = result.data,
                                error = result.message
                            )
                        }
                        is Resource.Loading -> {
                            breakingNews.value = breakingNews.value.copy(
                                isLoading = true,
                                news = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }

    private var searchJob: Job? = null

    fun searchNews(query: String) {
        searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            repository.searchNews(query, searchNewsPage)
                .onEach { result ->
                    when(result) {
                        is Resource.Success -> {
                            searchNewsPage++
                            if(searchNewsResponse == null) {
                                searchNewsResponse = result.data
                            } else {
                                val oldArticles = searchNewsResponse?.articles
                                val newArticles = result.data?.articles
                                if (newArticles != null) {
                                    oldArticles?.addAll(newArticles)
                                }
                            }
                            searchNews.value = searchNews.value.copy(
                                isLoading = false,
                                news = searchNewsResponse
                            )
                        }
                        is Resource.Error -> {
                            searchNews.value = searchNews.value.copy(
                                isLoading = false,
                                news = result.data,
                                error = result.message
                            )
                        }
                        is Resource.Loading -> {
                            searchNews.value = searchNews.value.copy(
                                isLoading = true,
                                news = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.addArticle(article)
    }

    fun getSavedNews() = repository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

}