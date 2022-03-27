package com.app.topupmama.apiSource.converter

import androidx.room.TypeConverter
import com.app.topupmama.apiSource.responseEntity.WeatherRemoteEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class WeatherObjectConverter {
    @TypeConverter
    fun fromString(value: String?): List<WeatherRemoteEntity.Object>? {
        val listType: Type = object : TypeToken<List<WeatherRemoteEntity.Object?>?>() {}.type
        return Gson().fromJson<List<WeatherRemoteEntity.Object>>(value, listType)
    }

    @TypeConverter
    fun listToString(list: List<WeatherRemoteEntity.Object?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}