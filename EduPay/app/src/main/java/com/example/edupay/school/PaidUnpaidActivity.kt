package com.example.edupay.school

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.conatctsync.app.EduPay
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.adapter.StudentAdapter
import com.example.edupay.databinding.ActivityPaidUnpaidBinding
import com.example.edupay.model.Student
import com.example.edupay.model.paid_unpaid.Payment
import com.example.edupay.model.school_register.School
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.ui.BaseActivity
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaidUnpaidActivity : BaseActivity() {
    private lateinit var binding: ActivityPaidUnpaidBinding
    private lateinit var adapter: StudentAdapter
    private var schoolData: School? = null
    private var currentTerm = 1
    private val allStudents = mutableListOf<Student>()
    private val students = mutableListOf<Student>()
    var myTinyDB: MyTinyDB? = null
    private val viewModel: HomeViewModel by viewModels()
    var preferenceHelper: PreferenceHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_payment_info)

        binding = ActivityPaidUnpaidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        schoolData = myTinyDB?.getSchoolData(Constants.SCHOOL_DATA)
        val emptyList = ArrayList<Payment>()
        adapter = StudentAdapter(emptyList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.btnTerm1.setOnClickListener {
            //  filterByTerm(1, it.payments)
            setSelectedButton(1)
            searchPayment("term1Fee", 1)
        }
        binding.btnTerm2.setOnClickListener {
            setSelectedButton(2)
            // filterByTerm(2, it.payments)
            searchPayment("term2Fee", 2)
        }
        binding.btnTerm3.setOnClickListener {
            setSelectedButton(3)
            //  filterByTerm(3)
            searchPayment("term3Fee", 3)
        }

        // default view
        setSelectedButton(1)
        searchPayment("term1Fee", 1)

    }


    private fun filterByTerm(term: Int, payments: ArrayList<Payment>?) {
        val filteredList = allStudents.filter { it.term == term }
        adapter.updateData(payments!!)
    }

    private fun setSelectedButton(term: Int) {
        binding.btnTerm1.isSelected = term == 1
        binding.btnTerm2.isSelected = term == 2
        binding.btnTerm3.isSelected = term == 3
    }

    private fun searchPayment(termType: String, terms: Int) {
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
        viewModel.searchPayment(schoolData!!.id, termType).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {
                println("Api Success response::" + it)
                val maidata = it.payments
                if (!maidata.isNullOrEmpty()) {
                    binding.tvNoData.visibility = View.GONE
                    filterByTerm(terms, maidata)
                } else {
                    filterByTerm(terms, it.payments)
                    binding.tvNoData.visibility = View.VISIBLE
                }

            }

        })
    }

}


