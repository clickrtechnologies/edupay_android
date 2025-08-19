package com.app.conatctsync.network

sealed class ApiResult<out T> {
    data class Success<out T>(val data :T) : ApiResult<T>()
    data class Error(val isNetworkError: Boolean,
                     val errorCode: Int?,
                     val errorBody: String?,
                     ) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}


