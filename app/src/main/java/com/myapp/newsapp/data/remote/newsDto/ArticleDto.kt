package com.myapp.newsapp.data.remote.newsDto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myapp.newsapp.domain.model.Article

@Entity(
    tableName = "articles"
)
data class ArticleDto(
    val author: String?,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: SourceDto,
    val title: String,
    val url: String,
    val urlToImage: String
) {
    fun toArticle(): Article {
        return Article(author, content, description, publishedAt, source, title, url, urlToImage)
    }
}