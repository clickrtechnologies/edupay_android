package com.app.conatctsync.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.app.conatctsync.utils.LocaleHelper
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class EduPay : Application() {
    companion object {
        lateinit var appContext: Context

        var application: EduPay? = null

        fun hasNetwork(): Boolean {
            val context = appContext ?: return false
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return false
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }



        fun getInstance(): EduPay {
            if (application == null) {
                throw IllegalStateException()
            }
            return application!!
        }
    }
    override fun onCreate() {
        super.onCreate()
      /*  if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }*/
        application=this
        appContext = applicationContext
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseApp.initializeApp(this)
      //  FirebaseApp.initializeApp(this)
     //   CognitoAuthManager.initialize(applicationContext)



    }




    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }






}

