package com.example.edupay.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.app.conatctsync.app.EduPay
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.databinding.OtpActivityBinding
import com.example.edupay.model.school_register.RegisterSchoolRequest
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.school.PaymentActivity
import com.example.edupay.school.SchoolDashboardActivity
import com.example.edupay.school.SchoolProfileActivity
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpActivity : BaseActivity() {
    var LOGIN_TYPE: String = Constants.LOGIN_TYPE_PARENT
    var EMAIL: String = ""
    var MOBILE: String = ""
    var COUNTRY_CODE: String = ""
    var OTP: String = ""
    var myTinyDB: MyTinyDB? = null
    var preferenceHelper: PreferenceHelper? = null
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: OtpActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.otp_activity)
        binding = OtpActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        if (intent != null) {
            if (intent.hasExtra(Constants.LOGIN_TYPE)) {
                LOGIN_TYPE = intent.getStringExtra(Constants.LOGIN_TYPE)!!
            }
            if (intent.hasExtra(Constants.EMAIL)) {
                EMAIL = intent.getStringExtra(Constants.EMAIL)!!
            }
            if (intent.hasExtra(Constants.MOBILE)) {
                MOBILE = intent.getStringExtra(Constants.MOBILE)!!
            }
            if (intent.hasExtra(Constants.COUNTRY_CODE)) {
                COUNTRY_CODE = intent.getStringExtra(Constants.COUNTRY_CODE)!!
            }
            if (intent.hasExtra(Constants.OTP)) {
                OTP = intent.getStringExtra(Constants.OTP)!!
            }

        }

        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        binding.ivLogo.startAnimation(animation)
        if (!OTP.isNullOrEmpty()) {
            binding.otpMsg.setText("For debugging purposes, your OTP is: $OTP")
        }

        binding.next.setOnClickListener {
            val enteredCode = binding.firstPinView.text.toString()
            if (enteredCode.equals(OTP)) {
                callFutherScreen()
            } else {
                Constants.showValidationDialog(this,getString(R.string.invalid_otp))
              //  Toast.makeText(this, getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show()
            }
        }
        binding.resendCode.setOnClickListener {
            if(LOGIN_TYPE.equals(Constants.SIGNUP_TYPE_SCHOOL)  )
            {
                resendOtpSchool(LOGIN_TYPE,EMAIL,MOBILE,COUNTRY_CODE)
            }else
            {
                resendOtpParents(LOGIN_TYPE,EMAIL,MOBILE,COUNTRY_CODE)

            }

        }


    }

    private fun callFutherScreen() {
        val schoolData = myTinyDB?.getSchoolData(Constants.SCHOOL_DATA)
        val parentData = myTinyDB?.getParentData(Constants.PARENT_DATA)

        when (LOGIN_TYPE) {
            Constants.SIGNUP_TYPE_SCHOOL -> {
                when {
                    schoolData == null || schoolData.name.isEmpty() -> {
                        goToSchoolProfile()
                    }

                    schoolData.subscribe == true -> {
                        goToSchoolDashboard()
                    }

                    else -> {
                        goToPayment()
                    }
                }
            }

            Constants.SIGNUP_TYPE_PARENT -> {
                val typeScreen=preferenceHelper!!.getValue(Constants.SCREEN_TYPE,Constants.LOGIN)
                if (typeScreen == null || typeScreen.equals(Constants.LOGIN)) {
                    preferenceHelper!!.save(Constants.SCREEN_TYPE,Constants.REG)
                    launchParentRegistration()
                } else {
                    preferenceHelper!!.save(Constants.SCREEN_TYPE,Constants.DASHBOARD)
                    goToSchoolDashboard()
                }
            }
        }
    }

/*    private fun callFutherScreen() {
        val schoolData = myTinyDB?.getSchoolData(Constants.SCHOOL_DATA)
        val parentData = myTinyDB?.getParentData(Constants.PARENT_DATA)

        when (LOGIN_TYPE) {
            Constants.SIGNUP_TYPE_SCHOOL -> {
                when {
                    schoolData == null || schoolData.name.isEmpty() -> {
                        goToSchoolProfile()
                    }

                    schoolData.subscribe == true -> {
                        goToSchoolDashboard()
                    }

                    else -> {
                        goToPayment()
                    }
                }
            }

            Constants.SIGNUP_TYPE_PARENT -> {
                if (parentData == null || parentData.fatherName.isEmpty()) {
                    launchParentRegistration()
                } else {
                    goToSchoolDashboard()
                }
            }
        }
    }*/
    private fun launchParentRegistration() {
        val intent = Intent(this, ParentRegistrationActivity::class.java).apply {
            putExtra(Constants.LOGIN_TYPE, LOGIN_TYPE)
            putExtra(Constants.EMAIL, EMAIL)
            putExtra(Constants.MOBILE, MOBILE)
        }
        startActivity(intent)
        finishAffinity()
    }
    // Helper methods for clarity
    private fun goToSchoolProfile() {
        val intent = Intent(this, SchoolProfileActivity::class.java).apply {
            putExtra(Constants.LOGIN_TYPE, LOGIN_TYPE)
            putExtra(Constants.MOBILE, MOBILE)
            putExtra(Constants.EMAIL, EMAIL)
            putExtra(Constants.SCREEN_TYPE, Constants.SCREEN_TYPE_SIGN_UP)
        }
        startActivity(intent)
        finish()
    }

    private fun goToSchoolDashboard() {
        val intent = Intent(this, SchoolDashboardActivity::class.java).apply {
            putExtra(Constants.LOGIN_TYPE, LOGIN_TYPE)
        }
        startActivity(intent)
        finish()
    }

    private fun goToPayment() {
        val intent = Intent(this, PaymentActivity::class.java).apply {
            putExtra(Constants.LOGIN_TYPE, LOGIN_TYPE)
        }
        startActivity(intent)
        finish()
    }

    private fun resendOtpSchool(
        type: String,
        email: String,
        mobileNumber: String,
        countryCode: String
    ) {

        if (!EduPay.hasNetwork()) {
            Dialogs.dialogWithCloseButton(
                activity = this,
                title = getString(R.string.network_title),
                msg = getString(R.string.network_error),
                btntext = getString(R.string.ok),
                cancelable = true
            )

        }
        showProgressDialog()
        val request =
            RegisterSchoolRequest(
                email,
                mobileNumber,
                countryCode,""
            )
        viewModel.loginSchool(request).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                it.school?.let { school ->
                    OTP= it.school.otp
                    Constants.showValidationDialog(this, getString(R.string.otp_sent))
                    binding.otpMsg.setText("For debugging purposes, your OTP is: $OTP")

                }
            } else {
                if (!it.note.isNullOrEmpty()) {
                    Constants.showValidationDialog(this, it.note)
                }
            }

        })
    }
    private fun resendOtpParents(
        type: String,
        email: String,
        mobileNumber: String,
        countryCode: String
    ) {

        if (!EduPay.hasNetwork()) {
            Dialogs.dialogWithCloseButton(
                activity = this,
                title = getString(R.string.network_title),
                msg = getString(R.string.network_error),
                btntext = getString(R.string.ok),
                cancelable = true
            )

        }
        showProgressDialog()
        val request =
            RegisterSchoolRequest(
                email,
                mobileNumber,
                countryCode,""
            )
        viewModel.resendOtpParents(request).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                it.parent?.let { school ->
                    OTP= it.parent.otp
                    Constants.showValidationDialog(this, getString(R.string.otp_sent))
                    binding.otpMsg.setText("For debugging purposes, your OTP is: $OTP")
                }
            } else {
                if (!it.note.isNullOrEmpty()) {
                    Constants.showValidationDialog(this, it.note)
                }
            }

        })
    }

}