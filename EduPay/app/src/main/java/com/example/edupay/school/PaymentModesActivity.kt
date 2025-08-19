package com.example.edupay.school

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.conatctsync.app.EduPay
import com.example.edupay.ui.BaseActivity
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.adapter.PaymentModesAdapter
import com.example.edupay.databinding.ActivityPaymentModBinding
import com.example.edupay.model.parent_register.Parent
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentModesActivity : BaseActivity() {
    private lateinit var binding: ActivityPaymentModBinding

    private var parentData: Parent?=null
    private val viewModel: HomeViewModel by viewModels()
    var myTinyDB: MyTinyDB? = null
    var preferenceHelper: PreferenceHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_payment_info)
        binding = ActivityPaymentModBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        parentData=myTinyDB!!.getParentData(Constants.PARENT_DATA)
        preferenceHelper = PreferenceHelper(this)
        getAllPaymentMethod()
    }


    private fun getAllPaymentMethod() {
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

        viewModel.getAllPaymentMethod().observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                if (!it.paymentOptions.isNullOrEmpty())
                {
                    val adapter = PaymentModesAdapter(it.paymentOptions)
                    binding.recyclerPaymentModes.adapter = adapter
                    binding.recyclerPaymentModes.layoutManager = LinearLayoutManager(this)
                }

            }

        })
    }

}