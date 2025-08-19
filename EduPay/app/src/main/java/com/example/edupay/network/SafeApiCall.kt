package com.app.conatctsync.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface SafeApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): ApiResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                ApiResult.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                   ApiResult.Error(false, throwable.code(), throwable.response()?.errorBody().toString())
                    }
                    else -> {
                        if (throwable.cause.toString().contains("java.lang.IllegalStateException")){
                            ApiResult.Error(false, 100, throwable.message)
                        }else{
                            ApiResult.Error(true, null, null)
                        }


                    }
                }
            }
        }
    }
}