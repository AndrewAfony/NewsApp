package com.myapp.newsapp.data.repository

import com.myapp.newsapp.data.local.NewsDatabase
import com.myapp.newsapp.data.remote.NewsApi
import com.myapp.newsapp.domain.model.Article
import com.myapp.newsapp.domain.model.NewsResponse
import com.myapp.newsapp.domain.repository.NewsRepository
import com.myapp.newsapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val db: NewsDatabase,
    private val api: NewsApi
): NewsRepository {

    override suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Flow<Resource<NewsResponse>> = flow {

        emit(Resource.Loading<NewsResponse>())

        try {
            val news = api.getBreakingNews(countryCode, pageNumber).toNewsResponse()
            emit(Resource.Success<NewsResponse>(data = news))
        } catch (e: HttpException) {
            emit(Resource.Error<NewsResponse>(message = e.localizedMessage ?: "Ops, something went wrong!"))
        } catch (e: IOException) {
            emit(Resource.Error<NewsResponse>(message = e.localizedMessage ?: "Couldn't reach server!"))
        }
    }

    override suspend fun searchNews(searchQuery: String, pageNumber: Int): Flow<Resource<NewsResponse>> = flow {

        emit(Resource.Loading<NewsResponse>())

        try {
            val news = api.searchForNews(searchQuery, pageNumber).toNewsResponse()
            emit(Resource.Success<NewsResponse>(data = news))
        } catch (e: HttpException) {
            emit(Resource.Error<NewsResponse>(message = e.localizedMessage ?: "Ops, something went wrong!"))
        } catch (e: IOException) {
            emit(Resource.Error<NewsResponse>(message = e.localizedMessage ?: "Couldn't reach server!"))
        }
    }

    override suspend fun addArticle(article: Article) {
        db.dao.addArticle(article)
    }

    override suspend fun deleteArticle(article: Article) {
        db.dao.deleteArticle(article)
    }

    override fun getSavedNews(): Flow<List<Article>> {
        return db.dao.getAllArticles()
    }
}