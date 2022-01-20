package com.myapp.newsapp.domain.repository

import com.myapp.newsapp.domain.model.Article
import com.myapp.newsapp.domain.model.NewsResponse
import com.myapp.newsapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Flow<Resource<NewsResponse>>

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Flow<Resource<NewsResponse>>

    suspend fun addArticle(article: Article)

    suspend fun deleteArticle(article: Article)

    fun getSavedNews(): Flow<List<Article>>

}