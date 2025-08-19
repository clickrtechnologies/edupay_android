package com.app.conatctsync.network

import androidx.lifecycle.Observer
import com.example.edupay.network.DataWrapper

class ApiObserver<T>(private val changeListener: ChangeListener<T>) : Observer<DataWrapper<T>?> {
    override fun onChanged(tDataWrapper: DataWrapper<T>?) {
        if (tDataWrapper != null) {
            if (tDataWrapper.apiException != null) {
                changeListener.onException(tDataWrapper.apiException)
            } else {
                changeListener.onSuccess(tDataWrapper.data)
            }
            return
        }
        //custom exceptionn to suite my use case
        //  changeListener.onException(new ValidationAPIException(ERROR_CODE, null));
    }

    interface ChangeListener<T> {
        fun onSuccess(dataWrapper: T)
        fun onException(exception: Exception?)
    }

    companion object {
        private const val ERROR_CODE = 0
    }
}