package com.app.topupmama.di

import com.app.topupmama.apiSource.WeatherRetrofit
import com.app.topupmama.localStorage.WeatherDao
import com.app.topupmama.presentation.weather.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun providesMainRepository(
        blogDao: WeatherDao,
        retrofit: WeatherRetrofit,
    ): Repository {
        return Repository(blogDao, retrofit)
    }
}