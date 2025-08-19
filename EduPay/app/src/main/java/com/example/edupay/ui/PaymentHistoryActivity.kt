package com.example.edupay.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.conatctsync.app.EduPay
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.adapter.PaymentHistoryAdapter
import com.example.edupay.databinding.ActivityPaymentHistoryBinding
import com.example.edupay.databinding.OtpActivityBinding
import com.example.edupay.model.parent_payment.PaymentListingData
import com.example.edupay.model.parent_register.Parent
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.school.PaymentActivity
import com.example.edupay.school.SchoolDashboardActivity
import com.example.edupay.school.SchoolProfileActivity
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentHistoryActivity : BaseActivity() {
    var myTinyDB: MyTinyDB? = null
    var preferenceHelper: PreferenceHelper? = null
    private val viewModel: HomeViewModel by viewModels()
    private var parentData: Parent?=null

    private lateinit var binding: ActivityPaymentHistoryBinding
    private lateinit var adapter: PaymentHistoryAdapter
    private val paymentList = mutableListOf<PaymentListingData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.otp_activity)
        binding = ActivityPaymentHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        parentData=myTinyDB!!.getParentData(Constants.PARENT_DATA)

        if (intent != null) {
           /* if (intent.hasExtra(Constants.LOGIN_TYPE)) {
                LOGIN_TYPE = intent.getStringExtra(Constants.LOGIN_TYPE)!!
            }*/


        }

        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        //binding.ivLogo.startAnimation(animation)

        setupToolbar()
        //setupRecyclerView()
        if(parentData!=null)
        {
            getParentPaymentDetailListing(parentData!!.id)
        }




    }
    private fun setupToolbar() {
        binding.tvTitle.text = getString(R.string.payment_history)
      //  binding.ivBack.setOnClickListener { finish() }
    }
    private fun setupRecyclerView() {
        adapter = PaymentHistoryAdapter(paymentList) { model ->
            val gson = Gson()
            val jsonString = gson.toJson(model)
            val intent = Intent(this, PaymentDetailsActivity::class.java)
            intent.putExtra(Constants.PAYMENT_LISTING_DATA, jsonString)
            startActivity(intent)
        }
        binding.recyclerPaymentHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerPaymentHistory.adapter = adapter
    }


    // Helper methods for clarity
    private fun goToHistoryDetail() {
        val intent = Intent(this, SchoolProfileActivity::class.java).apply {
            putExtra(Constants.SCREEN_TYPE, Constants.SCREEN_TYPE_SIGN_UP)
        }
        startActivity(intent)
        finish()
    }


    private fun getParentPaymentDetailListing(parentId:Int) {
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
        viewModel.getParentPaymentDetailListing(parentId).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                if (!it.payments.isNullOrEmpty())
                {
                    binding.tvNoData.visibility=View.GONE
                    paymentList.clear()
                    paymentList.addAll(it.payments)
                    setupRecyclerView()
                }

            }else {
                if (!it.note.isNullOrEmpty()) {
                    binding.tvNoData.visibility=View.VISIBLE
                    Constants.showValidationDialog(this, it.note)
                }
            }

        })
    }
}