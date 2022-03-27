package com.app.topupmama.apiSource.converter

import androidx.room.TypeConverter
import com.app.topupmama.apiSource.responseEntity.WeatherRemoteEntity.Object.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class WeatherConverter {
    @TypeConverter
    fun fromString(value: String?): List<Weather>? {
        val listType: Type = object : TypeToken<List<Weather?>?>() {}.type
        return Gson().fromJson<List<Weather>>(value, listType)
    }

    @TypeConverter
    fun listToString(list: List<Weather?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}