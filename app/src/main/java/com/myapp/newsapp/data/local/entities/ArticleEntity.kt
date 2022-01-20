package com.myapp.newsapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myapp.newsapp.data.remote.newsDto.SourceDto

@Entity(
    tableName = "articles"
)
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val author: String?,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: SourceDto,
    val title: String,
    val url: String,
    val urlToImage: String
)
