package com.example.edupay.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.app.conatctsync.utils.LocaleHelper
import com.bumptech.glide.Glide
import com.example.edupay.R
import com.example.edupay.databinding.ActivitySplashBinding
import com.example.edupay.model.parent_register.Parent
import com.example.edupay.model.school_register.School
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.school.PaymentActivity
import com.example.edupay.school.SchoolDashboardActivity
import com.example.edupay.school.SchoolProfileActivity
import com.example.edupay.utils.Constants

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    var myTinyDB: MyTinyDB? = null
    var preferenceHelper: PreferenceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ViewBinding
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)


        Glide.with(this)
            .asGif()
            .load(R.drawable.splash_gif) // can also be URL or file path
            .into(binding.gifImageView)
        /*    binding.splashText.alpha = 0f
            binding.splashText.animate()
                .alpha(1f)
                .setDuration(2000)
                .start()*/

        Handler(Looper.getMainLooper()).postDelayed({
            val lang = PreferenceHelper(this).getValue(Constants.SELECTED_LANGUAGE, "en")
            LocaleHelper.setLocale(this, lang)
            callFutherSCreen()
            /* startActivity(Intent(this, LoginTypeActivity::class.java))
             finish()*/
        }, 3000)
    }

    private fun callFutherSCreen() {
        val schoolData = myTinyDB?.getSchoolData(Constants.SCHOOL_DATA)
        val parentData = myTinyDB?.getParentData(Constants.PARENT_DATA)
        val LOGIN_TYPE = preferenceHelper!!.getValue(Constants.LOGIN_TYPE, "")
        val typeScreen = preferenceHelper!!.getValue(Constants.SCREEN_TYPE, Constants.LOGIN)

        when (LOGIN_TYPE) {
            Constants.SIGNUP_TYPE_SCHOOL -> {
                when {
                    schoolData == null || schoolData.name.isEmpty() -> {
                        goToSchoolProfile(schoolData)
                    }

                    schoolData.name == null -> {
                        goToSchoolProfile(schoolData)
                    }

                    schoolData.subscribe -> {
                        goToSchoolDashboard(Constants.SIGNUP_TYPE_SCHOOL)
                    }

                    else -> {
                        goToSchoolProfile(schoolData)
                        // goToPayment()
                    }
                }
            }

            Constants.SIGNUP_TYPE_PARENT -> {
                val ScreenType = preferenceHelper!!.getValue(Constants.SCREEN_TYPE, Constants.LOGIN)

                if (ScreenType.equals(Constants.DASHBOARD)) {
                    goToSchoolDashboard(Constants.SIGNUP_TYPE_PARENT)
                } else if (ScreenType.equals(Constants.REG)) {
                    goToParentProfile(parentData)
                } else {
                    goToParentLogin()
                }

                /*            when {
                                parentData == null -> {
                                    goToParentLogin()
                                }

                                parentData.fatherName == null -> {
                                    goToParentProfile(parentData)
                                }

                                parentData.photoPath == null -> {
                                    goToParentLogo(parentData)
                                }

                                else -> {

                                    goToSchoolDashboard(Constants.SIGNUP_TYPE_PARENT)

                                }
                            }*/
            }

            "" -> {
                val intent = Intent(this, LoginTypeActivity::class.java).apply {
                    putExtra(Constants.SCREEN_TYPE, Constants.SPLASH)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun goToParentProfile(parent: Parent?) {
        val intent = Intent(this, ParentRegistrationActivity::class.java).apply {
            putExtra(Constants.LOGIN_TYPE, Constants.SIGNUP_TYPE_PARENT)
            putExtra(Constants.EMAIL, parent?.email)
            putExtra(Constants.MOBILE, parent!!.countryCode + parent!!.mobileNumber)
        }
        startActivity(intent)
        finish()
    }

    private fun goToParentLogo(parent: Parent?) {
        val intent = Intent(this, ParentRegistrationActivity::class.java).apply {
            putExtra(Constants.LOGIN_TYPE, Constants.SIGNUP_TYPE_PARENT)
            putExtra(Constants.EMAIL, parent?.email)
            putExtra(Constants.MOBILE, parent!!.countryCode + parent!!.mobileNumber)
        }
        startActivity(intent)
        finish()
    }

    private fun goToParentLogin() {
        val intent = Intent(this, ParentLoginActivity::class.java)
        intent.putExtra(Constants.LOGIN_TYPE, Constants.LOGIN_TYPE_PARENT)
        startActivity(intent)
        finish()
    }

    // Helper methods for clarity
    private fun goToSchoolProfile(schoolData: School?) {
        val intent = Intent(this, SchoolProfileActivity::class.java).apply {
            putExtra(Constants.LOGIN_TYPE, Constants.SIGNUP_TYPE_SCHOOL)
            putExtra(Constants.MOBILE, "${schoolData!!.countryCode}${schoolData!!.mobileNumber}")
            putExtra(Constants.EMAIL, schoolData.email)
            putExtra(Constants.SCREEN_TYPE, Constants.SCREEN_TYPE_SIGN_UP)
        }
        startActivity(intent)
        finish()
    }

    private fun goToSchoolDashboard(userType: String) {
        val intent = Intent(this, SchoolDashboardActivity::class.java).apply {
            putExtra(Constants.LOGIN_TYPE, userType)
        }
        startActivity(intent)
        finish()
    }

    private fun goToPayment() {
        val intent = Intent(this, PaymentActivity::class.java).apply {
            putExtra(Constants.LOGIN_TYPE, Constants.SCREEN_TYPE_SIGN_UP)
        }
        startActivity(intent)
        finish()
    }
}