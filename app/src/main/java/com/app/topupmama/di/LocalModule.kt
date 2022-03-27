package com.app.topupmama.di

import android.content.Context
import androidx.room.Room
import com.app.topupmama.localStorage.WeatherDao
import com.app.topupmama.localStorage.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object LocalModule {

    @Singleton
    @Provides
    fun provideBlogDb(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            WeatherDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBlogDao(blogDatabase: WeatherDatabase): WeatherDao {
        return blogDatabase.blogDao()
    }

}