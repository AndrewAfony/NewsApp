package com.myapp.newsapp.data.remote.newsDto

import com.myapp.newsapp.domain.model.NewsResponse

data class NewsResponseDto(
    val articles: List<ArticleDto>,
    val status: String,
    val totalResults: Int
) {
    fun toNewsResponse(): NewsResponse {
        return NewsResponse(articles, status, totalResults)
    }
}
