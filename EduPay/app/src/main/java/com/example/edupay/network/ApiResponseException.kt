package com.app.conatctsync.network

class ApiResponseException(val statusCode: Int, message: String?) : Throwable(message)