package com.app.topupmama.apiSource.responseEntity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.app.topupmama.apiSource.converter.WeatherConverter
import com.app.topupmama.apiSource.converter.WeatherObjectConverter
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "weather_table")
data class WeatherRemoteEntity(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("cnt")
    val cnt: Int = 0,
    @TypeConverters(WeatherObjectConverter::class)
    @SerializedName("list")
    val list: List<Object> = listOf()
): Serializable {
    @Entity(tableName = "favorite_table")
    data class Object(
        @PrimaryKey(autoGenerate = false)
        val keyId: Int = 1,
        @SerializedName("clouds")
        @Embedded
        val clouds: Clouds = Clouds(),
        @SerializedName("coord")
        @Embedded
        val coord: Coord = Coord(),
        @SerializedName("dt")
        val dt: Double = 0.0,
        @SerializedName("id")
        val id: Int = 0,
        @Embedded
        @SerializedName("main")
        val main: Main = Main(),
        @SerializedName("name")
        val name: String = "",
        @Embedded
        @SerializedName("sys")
        val sys: Sys = Sys(),
        @SerializedName("visibility")
        val visibility: Double = 0.0,
        @TypeConverters(WeatherConverter::class)
        @SerializedName("weather")
        val weather: List<Weather> = listOf(),
        @Embedded
        @SerializedName("wind")
        val wind: Wind = Wind()
    ):Serializable {
        @Entity
        data class Clouds(
            @SerializedName("all")
            val all: Double = 0.0
        ):Serializable

        @Entity
        data class Coord(
            @SerializedName("lat")
            val lat: Double = 0.0,
            @SerializedName("lon")
            val lon: Double = 0.0
        ):Serializable

        @Entity
        data class Main(
            @SerializedName("feels_like")
            val feelsLike: Double = 0.0,
            @SerializedName("grnd_level")
            val grndLevel: Double = 0.0,
            @SerializedName("humidity")
            val humidity: Double = 0.0,
            @SerializedName("pressure")
            val pressure: Double = 0.0,
            @SerializedName("sea_level")
            val seaLevel: Double = 0.0,
            @SerializedName("temp")
            val temp: Double = 0.0,
            @SerializedName("temp_max")
            val tempMax: Double = 0.0,
            @SerializedName("temp_min")
            val tempMin: Double = 0.0
        ):Serializable

        @Entity
        data class Sys(
            @SerializedName("country")
            val country: String = "",
            @SerializedName("sunrise")
            val sunrise: Int = 0,
            @SerializedName("sunset")
            val sunset: Double = 0.0,
            @SerializedName("timezone")
            val timezone: Int = 0
        ):Serializable

        @Entity
        data class Weather(
            @SerializedName("description")
            val description: String = "",
            @SerializedName("icon")
            val icon: String = "",
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("main")
            val main: String = ""
        ):Serializable

        @Entity
        data class Wind(
            @SerializedName("deg")
            val deg: Double = 0.0,
            @SerializedName("speed")
            val speed: Double = 0.0
        ):Serializable
    }
}