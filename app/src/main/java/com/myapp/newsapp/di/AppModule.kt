package com.myapp.newsapp.di

import android.app.Application
import androidx.room.Room
import com.myapp.newsapp.BuildConfig
import com.myapp.newsapp.data.local.NewsDao
import com.myapp.newsapp.data.local.NewsDatabase
import com.myapp.newsapp.data.remote.NewsApi
import com.myapp.newsapp.data.repository.NewsRepositoryImpl
import com.myapp.newsapp.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.NEWS_BASE_URL)
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(context: Application): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "news_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsRepository(db: NewsDatabase, api: NewsApi): NewsRepository {
        return NewsRepositoryImpl(db, api)
    }

}