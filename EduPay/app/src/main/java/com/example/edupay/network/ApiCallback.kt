package com.app.conatctsync.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class ApiCallback<T> : Callback<Response<T>?> {
    override fun onResponse(call: Call<Response<T>?>, response: Response<Response<T>?>) {
        if (response.body() != null) {
            handleResponseData(response.body()!!.body())
        } else {
            handleError(response)
        }
    }

    protected abstract fun handleResponseData(data: T?)
    protected abstract fun handleError(response: Response<Response<T>?>?)
    protected abstract fun handleException(t: Exception?)
    override fun onFailure(call: Call<Response<T>?>, t: Throwable) {
        if (t is Exception) {
            handleException(t)
        } else {
            //do something else
        }
    }
}