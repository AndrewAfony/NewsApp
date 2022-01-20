package com.myapp.newsapp.domain.model

import com.myapp.newsapp.data.local.entities.ArticleEntity
import com.myapp.newsapp.data.remote.newsDto.SourceDto


data class Article(
    val author: String?,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: SourceDto,
    val title: String,
    val url: String,
    val urlToImage: String
) {
    fun toArticleEntity(): ArticleEntity {
        return ArticleEntity(
            author = author,
            content = content,
            description = description,
            publishedAt = publishedAt,
            source = source,
            title = title,
            url = url,
            urlToImage = urlToImage
        )
    }
}
