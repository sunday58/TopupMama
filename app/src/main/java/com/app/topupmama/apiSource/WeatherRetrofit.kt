package com.app.topupmama.apiSource


import com.app.topupmama.apiSource.responseEntity.WeatherRemoteEntity
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherRetrofit {

    @GET("group")
    suspend fun weather(@Query("id") id: String, @Query("units") units: String,
                        @Query("appid") key: String): ApiResponse<WeatherRemoteEntity>
}