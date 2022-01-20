package com.myapp.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myapp.newsapp.data.local.entities.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class NewsDatabase: RoomDatabase() {

    abstract val dao: NewsDao
}