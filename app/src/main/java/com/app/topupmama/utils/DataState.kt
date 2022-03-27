package com.app.topupmama.utils

sealed class DataState<out R> {
    data class success<out T>(val data: T): DataState<T>()
    data class Error(val exception: Throwable): DataState<Nothing>()
    object Loading: DataState<Nothing>()
    data class otherError(val error: String): DataState<Nothing>()

}