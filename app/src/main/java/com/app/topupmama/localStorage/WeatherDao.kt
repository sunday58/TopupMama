package com.app.topupmama.localStorage

import androidx.room.*
import com.app.topupmama.apiSource.responseEntity.WeatherRemoteEntity

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blogEntity: WeatherRemoteEntity)

    @Query("SELECT * FROM weather_table")
    suspend fun get(): WeatherRemoteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favorite(favoriteEntity: WeatherRemoteEntity.Object)

    @Query("SELECT * FROM favorite_table")
    suspend fun getFavorite(): WeatherRemoteEntity.Object

}