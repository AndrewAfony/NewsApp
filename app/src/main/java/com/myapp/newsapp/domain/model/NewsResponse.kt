package com.myapp.newsapp.domain.model

import com.myapp.newsapp.data.remote.newsDto.ArticleDto

data class NewsResponse(
    val articles: List<ArticleDto>,
    val status: String,
    val totalResults: Int
)
