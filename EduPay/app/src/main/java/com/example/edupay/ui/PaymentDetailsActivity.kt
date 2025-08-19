package com.example.edupay.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.example.edupay.R
import com.example.edupay.databinding.ActivityPaymentDetailsBinding
import com.example.edupay.databinding.OtpActivityBinding
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

@AndroidEntryPoint
class PaymentDetailsActivity : BaseActivity() {
    var myTinyDB: MyTinyDB? = null
    var payment_listing_data: String? = null
    var preferenceHelper: PreferenceHelper? = null
    private lateinit var binding: ActivityPaymentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.otp_activity)
        binding = ActivityPaymentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        if (intent != null) {
            if (intent.hasExtra(Constants.PAYMENT_LISTING_DATA)) {
                payment_listing_data = intent.getStringExtra(Constants.PAYMENT_LISTING_DATA)!!

            }


        }

        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        binding.imageViewLogo.startAnimation(animation)

        val convertedObject: PaymentListingData = fromJson(payment_listing_data!!)
        if (convertedObject != null) {
            bindData(convertedObject)
        }

        /*     val history = intent.getParcelableExtra<PaymentHistory>("history")

             history?.let {
                 binding.apply {
                     tvDate.text = it.date
                     tvAmount.text = getString(R.string.rupee_with_amount, it.amount)
                     tvTransactionId.text = getString(R.string.transaction_id_format, it.transactionId)
                     tvPaymentStatus.text = getString(
                         if (it.status == "success") R.string.payment_success else R.string.payment_failed
                     )
                 }
             }*/


    }


    fun fromJson(json: String): PaymentListingData {
        val type = object : TypeToken<PaymentListingData>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun bindData(data: PaymentListingData) {
        with(binding) {
            tvStudentName.text =
                "${getString(R.string.label_student_name)}: ${data.studentName.orEmpty()}"
            tvSchoolName.text =
                "${getString(R.string.label_school_name)}: ${data.schoolName.orEmpty()}"
            tvPaymentDate.text =
                "${getString(R.string.label_payment_date)}: ${data.paymentDate.orEmpty()}"
            tvAmountPaid.text =
                "${getString(R.string.label_amount_paid)}: ${data.currency.orEmpty()} ${data.amountPaid}"
            tvCurrency.text = "${getString(R.string.label_currency)} ${data.currency.orEmpty()}"
            tvPaymentMethod.text =
                "${getString(R.string.label_payment_method)}: ${data.paymentMethod.orEmpty()}"
            tvStartDate.text = "${getString(R.string.label_start_date)}: ${data.startDate.orEmpty()}"
            tvEndDate.text = "${getString(R.string.label_end_date)}: ${data.endDate.orEmpty()}"
            tvStatus.text = "${getString(R.string.label_payment_status)}: ${data.status.orEmpty()}"
            tvTransactionId.text =
                "${getString(R.string.label_transaction_id)}: ${data.transectionId.orEmpty()}"

            // Updated fee breakdown
            tvTuitionFee.text = "${getString(R.string.label_tuition_fee)}: $${data.tuitionFee}"
            tvTransportationFee.text =
                "${getString(R.string.label_transportation_fee)}: $${data.transportationFee}"
            tvUniformFee.text = "${getString(R.string.label_uniform_fee)}: $${data.uniformFee}"
            tvStationaryFee.text =
                "${getString(R.string.label_stationary_fee)}: $${data.stationaryFee}"
            tvServiceCharges.text =
                "${getString(R.string.label_service_charges)}: $${data.serviceCharges}"
            tvTerm1Fee.text = "${getString(R.string.label_term1_fee)}: $${data.term1Fee}"
            tvTerm2Fee.text = "${getString(R.string.label_term2_fee)}: $${data.term2Fee}"
            tvTerm3Fee.text = "${getString(R.string.label_term3_fee)}: $${data.term3Fee}"
        }
    }


}