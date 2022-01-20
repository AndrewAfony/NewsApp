package com.myapp.newsapp.presentation

import com.myapp.newsapp.domain.model.NewsResponse

data class NewsState(
    val isLoading: Boolean = false,
    val news: NewsResponse? = null,
    val error: String? = null
)
