package com.example.edupay.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.conatctsync.utils.LocaleHelper
import com.example.edupay.R
import com.example.edupay.adapter.StudentActivityAdapter
import com.example.edupay.databinding.ActivityLanguageSelectionBinding
import com.example.edupay.databinding.ActivityPaymentDetailsBinding
import com.example.edupay.databinding.ActivityStudentActivityBinding
import com.example.edupay.databinding.OtpActivityBinding
import com.example.edupay.model.StudentEvent
import com.example.edupay.model.parent_payment.PaymentListingData
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.school.PaymentActivity
import com.example.edupay.school.SchoolDashboardActivity
import com.example.edupay.school.SchoolProfileActivity
import com.example.edupay.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class LanguageSelectionActivity : BaseActivity() {
    var myTinyDB: MyTinyDB? = null
    var SCREEN_TYPE: String? = ""
    var preferenceHelper: PreferenceHelper? = null
    private lateinit var binding: ActivityLanguageSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.otp_activity)
        binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        if (intent != null) {
            if (intent.hasExtra(Constants.SCREEN_TYPE)) {
                SCREEN_TYPE = intent.getStringExtra(Constants.SCREEN_TYPE)!!

            }
        }

        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        binding.imgLogo.startAnimation(animation)

        binding.btnEnglish.setOnClickListener {
            setLocaleAndContinue("en")
        }

        binding.btnFrench.setOnClickListener {
            setLocaleAndContinue("fr")
        }

        binding.btnSwahili.setOnClickListener {
            setLocaleAndContinue("sw")  // 'sw' is the ISO 639-1 language code for Swahili
        }


    }

    var count = 0
    private fun setLocaleAndContinue(language: String) {
        // Save language preference
        preferenceHelper?.save(Constants.SELECTED_LANGUAGE, language)

        // Apply immediately
        LocaleHelper.setLocale(this, language)

        // Restart the app properly
        restartApp()
    }


    private fun restartApp() {
        val intent = Intent(this,
            if (SCREEN_TYPE == Constants.SPLASH) LoginTypeActivity::class.java
            else SchoolDashboardActivity::class.java
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finishAffinity()

        // Add a small delay to ensure smooth transition
        Handler(Looper.getMainLooper()).postDelayed({
            // Force recreate to ensure all activities get the new locale
            recreate()
        }, 100)
    }


    private fun saveLanguageToPreferences(language: String) {
        preferenceHelper!!.save(Constants.SELECTED_LANGUAGE, language)
    }

 /*   override fun attachBaseContext(newBase: Context) {
        val preferenceHelper = PreferenceHelper(newBase)
        val lang = preferenceHelper.getValue(Constants.SELECTED_LANGUAGE, "en")
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }*/

    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }
}