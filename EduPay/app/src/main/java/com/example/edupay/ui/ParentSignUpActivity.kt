package com.example.edupay.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.app.conatctsync.app.EduPay
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.databinding.ActivityParentSignupBinding
import com.example.edupay.model.school_register.RegisterSchoolRequest
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParentSignUpActivity : BaseActivity() {
    private lateinit var binding: ActivityParentSignupBinding
    private val viewModel: HomeViewModel by viewModels()
    var myTinyDB: MyTinyDB? = null
    var preferenceHelper: PreferenceHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_main)

        binding = ActivityParentSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        binding.ivLogo.startAnimation(animation)
        // Button Click
        binding.btnPayFees.setOnClickListener {


            if (validateInputs()) {

                val mobile = binding.etMobile.text.toString().trim()
                val email = binding.etEmail.text.toString().trim()
                registeredParent(email,mobile,binding.ccp.selectedCountryCodeWithPlus)
            }



        }

        binding.tvRegisterSchool.setOnClickListener {
            startActivity(Intent(this, ParentLoginActivity::class.java))
        }

    }


    private fun registeredParent(
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
        viewModel.registeredParent(request).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {
                if(it.child!=null)
                    myTinyDB!!.putChildData(Constants.CHILD_DATA_LIST, it.child)

                if(it.paymentMehod!=null)
                    myTinyDB!!.putPaymentMethod(Constants.PAYMENT_METHOD, it.paymentMehod)

                it.parent?.let { school ->
                    myTinyDB!!.putParentData(Constants.PARENT_DATA, school)
                    preferenceHelper!!.save(Constants.ACCESS_TOKEN, it.access_token)
                    preferenceHelper!!.save(Constants.LOGIN_TYPE, Constants.SIGNUP_TYPE_PARENT)
                    var otp = "1234"
                    if (it.parent?.otp != null) {
                        otp = if (it.parent.otp.length > 4) {
                            it.parent.otp.dropLast(2)
                        } else {
                            it.parent.otp
                        }
                    }
                    val intent = Intent(this, OtpActivity::class.java).apply {
                        putExtra(Constants.MOBILE, mobileNumber)
                        putExtra(Constants.OTP, otp)
                        putExtra(Constants.COUNTRY_CODE, countryCode)
                        //   putExtra("schoolName", schoolName)
                        putExtra(Constants.EMAIL, email)
                        putExtra(Constants.LOGIN_TYPE, Constants.SIGNUP_TYPE_PARENT)
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
    private fun validateInputs(): Boolean {
        val schoolId = binding.etEmail.text.toString().trim()
        val mobile = binding.etMobile.text.toString().trim()

        return when {
            schoolId.isEmpty() -> {
                Toast.makeText(this, getString(R.string.valid_email_err), Toast.LENGTH_SHORT).show()
                false
            }

            mobile.isEmpty() -> {
                Toast.makeText(this, getString(R.string.valid_mobile_err), Toast.LENGTH_SHORT).show()
                false
            }

            mobile.length < 7 -> {
                Toast.makeText(this,  getString(R.string.valid_mobile_err1), Toast.LENGTH_SHORT).show()
                false
            }

            else -> true
        }
    }
}