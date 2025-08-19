package com.example.edupay.school

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.databinding.ActivityPaymentBinding
import com.example.edupay.model.school_register.School
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import com.example.edupay.R
import com.example.edupay.utils.Dialogs

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {
    private var schoolData: School? = null
    private lateinit var binding: ActivityPaymentBinding
    private val viewModel: HomeViewModel by viewModels()
    private var myTinyDB: MyTinyDB? = null
    private var schoolName: String = ""
    private var mobileAccount: String = ""
    private var preferenceHelper: PreferenceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        schoolData = myTinyDB?.getSchoolData(Constants.SCHOOL_DATA)
//Row No--
        // Setup plan spinner from string resources
        val planOptions = listOf(
            getString(R.string.plan_monthly),
            getString(R.string.plan_quarterly),
            getString(R.string.plan_half_yearly),
            getString(R.string.plan_yearly)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, planOptions)
        binding.spinnerPlan.adapter = adapter

        // Set school info
        if (schoolData != null) {
            schoolName = schoolData!!.name
            mobileAccount = schoolData!!.moneyAccount
        }

        binding.tvFromSchoolInfo.text = "$schoolName\n${getString(R.string.from_mobile, mobileAccount)}"
        binding.tvToAccountInfo.text = getString(R.string.to_info)

        // Submit button logic
        binding.btnSubmitPayment.setOnClickListener {
            binding.btnSubmitPayment.isEnabled = false
            binding.btnSubmitPayment.text = getString(R.string.processing)

            processPayment { isSuccess ->
                binding.btnSubmitPayment.isEnabled = true
                binding.btnSubmitPayment.text = getString(R.string.submit_payment)
                showSuccessDialog()
            /*    if (isSuccess) {
                    showSuccessDialog()
                } else {
                    showFailureDialog()
                }*/
            }
        }
    }

    private fun processPayment(onResult: (Boolean) -> Unit) {
        val success = listOf(true, false).random()
        binding.btnSubmitPayment.postDelayed({
            onResult(success)
        }, 2000)
    }

    private fun showFailureDialog() {
        Dialogs.dialogWithCloseButton(
            activity = this,
            msg = getString(R.string.payment_failed_msg),
            btntext = getString(R.string.retry),
            cancelable = true
        ){

        }

    }

    private fun showSuccessDialog() {
        Dialogs.dialogWithCloseButton(
            this,
            getString(R.string.payment_success_msg),
            getString(R.string.ok),
            false,
        ) {
            // Navigate to School Dashboard
            val intent = Intent(this, SchoolDashboardActivity::class.java)
            intent.putExtra(Constants.LOGIN_TYPE, Constants.SIGNUP_TYPE_SCHOOL)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            // Save updated subscription data
            val updatedSchoolData = schoolData?.copy(subscribe = true, otpExpiryTime = schoolData!!.otpExpiryTime ?: "")
            updatedSchoolData?.let {
                myTinyDB?.putSchoolData(Constants.SCHOOL_DATA, it)
            }
        }
    }


}
