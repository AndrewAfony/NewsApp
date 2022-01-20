package com.myapp.newsapp.data.local

import androidx.room.*
import com.myapp.newsapp.data.local.entities.ArticleEntity
import kotlinx.coroutines.flow.StateFlow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArticle(article: ArticleEntity): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): StateFlow<List<ArticleEntity>>

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)

}