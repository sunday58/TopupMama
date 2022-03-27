package com.app.topupmama.localStorage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.topupmama.apiSource.converter.WeatherConverter
import com.app.topupmama.apiSource.converter.WeatherObjectConverter
import com.app.topupmama.apiSource.responseEntity.WeatherRemoteEntity

@Database(
    entities = [WeatherRemoteEntity::class, WeatherRemoteEntity.Object::class],
    version = 1
)
@TypeConverters(WeatherObjectConverter::class, WeatherConverter::class)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun blogDao(): WeatherDao

    companion object{
        val DATABASE_NAME: String = "blog_db"
    }

}