package com.example.edupay.school

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.app.conatctsync.app.EduPay
import com.example.edupay.ui.BaseActivity
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.ui.OtpActivity
import com.example.edupay.R
import com.example.edupay.databinding.ActivitySchoolLoginBinding
import com.example.edupay.model.school_register.RegisterSchoolRequest
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SchoolLoginActivity : BaseActivity() {
    private lateinit var binding: ActivitySchoolLoginBinding
    private val viewModel: HomeViewModel by viewModels()
    var myTinyDB: MyTinyDB? = null
    var preferenceHelper: PreferenceHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivitySchoolLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        binding.btnSchoolLogin.setOnClickListener {
            if (validateInputs()) {

                val fullPhoneNumber = binding.etMobile.text.toString()
                val email = binding.etEmail.text.toString().trim()
                loginSchool(email, fullPhoneNumber, binding.ccp.selectedCountryCodeWithPlus)

            }
        }
        binding.tvRegisterSchool.setOnClickListener {
            startActivity(Intent(this, SchoolRegistrationActivity::class.java))
            finish()
        }

        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        binding.ivLogo.startAnimation(animation)
    }

    private fun validateInputs(): Boolean {
        val schoolId = binding.etEmail.text.toString().trim()
        val mobile = binding.etMobile.text.toString().trim()

        return when {
            schoolId.isEmpty() -> {
                Constants.showValidationDialog(this, getString(R.string.valid_email_reg_err))
                false
            }

            mobile.isEmpty() -> {
                Constants.showValidationDialog(this, getString(R.string.valid_mobile_err))

                false
            }

            mobile.length < 7 -> {
                Constants.showValidationDialog(this, getString(R.string.valid_mobile_err1))
                false
            }

            else -> true
        }
    }
    private fun loginSchool(
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
                    myTinyDB!!.putSchoolData(Constants.SCHOOL_DATA, school)
                    preferenceHelper!!.save(Constants.ACCESS_TOKEN, it.access_token)
                    preferenceHelper!!.save(Constants.LOGIN_TYPE, Constants.SIGNUP_TYPE_SCHOOL)
                    var otp = "1234"
                    if (it.school?.otp != null) {
                        otp = if (it.school.otp.length > 4) {
                            it.school.otp.dropLast(2)
                        } else {
                            it.school.otp
                        }
                    }
                    val intent = Intent(this, OtpActivity::class.java).apply {
                        putExtra(Constants.MOBILE, mobileNumber)
                        putExtra(Constants.OTP, otp)
                        putExtra(Constants.COUNTRY_CODE, countryCode)
                        //   putExtra("schoolName", schoolName)
                        putExtra(Constants.EMAIL, email)
                        putExtra(Constants.LOGIN_TYPE, Constants.SIGNUP_TYPE_SCHOOL)
                    }
                    startActivity(intent)
                    //  preferenceHelper!!.save(Constant.SESSION_ID, body.data.session)
                    // preferenceHelper!!.save(Constant.CHALLENGE_NAME, body.data.challengeName)
                }
            } else {
                if (!it.note.isNullOrEmpty()) {
                    Constants.showValidationDialog(this, it.note)
                }
            }

        })
    }


}