package com.myapp.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.newsapp.domain.model.NewsResponse
import com.myapp.newsapp.domain.repository.NewsRepository
import com.myapp.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {

    var breakingNews = MutableStateFlow(NewsState())
    private set

    init {
        getBreakingNews("ru")
    }

    fun getBreakingNews(countryCode: String) {
        viewModelScope.launch {
            repository.getBreakingNews(countryCode, 1)
                .onEach {result ->
                    when(result) {
                        is Resource.Success -> {
                            breakingNews.value = breakingNews.value.copy(
                                isLoading = false,
                                news = result.data
                            )
                        }
                        is Resource.Error -> {
                            breakingNews.value = breakingNews.value.copy(
                                isLoading = false,
                                news = result.data
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
    }

}