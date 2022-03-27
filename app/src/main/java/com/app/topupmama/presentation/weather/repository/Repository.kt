package com.app.topupmama.presentation.weather.repository

import android.util.Log
import com.app.topupmama.apiSource.WeatherRetrofit
import com.app.topupmama.apiSource.responseEntity.WeatherRemoteEntity
import com.app.topupmama.localStorage.WeatherDao
import com.app.topupmama.utils.DataState
import com.skydoves.sandwich.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Repository
    constructor(
        private val blogDao: WeatherDao,
        private val blogRetrofit: WeatherRetrofit,
    )
{
        suspend fun getBlog(id: String, units: String, key: String): Flow<DataState<WeatherRemoteEntity>> = flow {
            DataState.Loading
            delay(timeMillis = 1000)
            val response = blogRetrofit.weather(id, units, key)
            response.suspendOnSuccess {
                val  networkBlogs = data
                blogDao.insert(networkBlogs)

                val cacheBlogs = blogDao.get()
                emit(DataState.success(cacheBlogs))
            }
            response.suspendOnError{
                when (statusCode){
                    StatusCode.Unauthorized -> emit(DataState.otherError("token time out"))
                    StatusCode.BadGateway -> emit(DataState.otherError("Something went wrong"))
                    StatusCode.GatewayTimeout -> emit(DataState.otherError("Unable to fetch data, please try again"))
                    else -> emit(DataState.otherError(message()))
                }
            }
            response.suspendOnException {
                if (exception.message!!.contains("Unable to resolve host")) {
                    emit(DataState.otherError("we are unable to process your request, please try again later"))
                }else{
                    Log.d("message", exception.message!!)
                    emit(DataState.Error(exception))
                }

            }

        }

    suspend fun updateFavorite(favoriteEntity: WeatherRemoteEntity.Object, result: (WeatherRemoteEntity.Object?) -> Unit){
        blogDao.favorite(favoriteEntity)
        result(blogDao.getFavorite())
    }

    suspend fun getFavorite(result: (WeatherRemoteEntity.Object?) -> Unit){
        result(blogDao.getFavorite())
    }
    }
