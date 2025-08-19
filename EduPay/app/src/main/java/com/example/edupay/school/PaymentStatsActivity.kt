package com.example.edupay.school

// PaymentStatsActivity.kt

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.conatctsync.app.EduPay
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.adapter.StudentPaymentAdapter
import com.example.edupay.databinding.ActivityPaymentStatsBinding
import com.example.edupay.model.school_register.School
import com.example.edupay.model.school_stats.ReceivedStatics
import com.example.edupay.model.school_stats.Student
import com.example.edupay.model.school_stats.due.DueStatics
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.ui.BaseActivity
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentStatsActivity : BaseActivity() {

    private var adapter: StudentPaymentAdapter?=null
    private lateinit var binding: ActivityPaymentStatsBinding
    private var schoolData: School? = null
    enum class TabType { DUE, RECEIVED }
    private var currentTab = TabType.DUE
    var preferenceHelper: PreferenceHelper? = null
    private val viewModel: HomeViewModel by viewModels()
    var myTinyDB: MyTinyDB? = null
    private val students = mutableListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        schoolData = myTinyDB?.getSchoolData(Constants.SCHOOL_DATA)
        // Dummy data for students
        val modes = listOf("Cash", "Mobile Money", "Bank Transfer")
     /*   for (i in 1..100) {
            val isPaid = i % 4 != 0
            val mode = if (isPaid) modes.random() else null
            students.add(StudentChart(i, "Student $i", isPaid, mode))
        }*/

        // Init recycler view
        binding.recyclerViewPayments.layoutManager = LinearLayoutManager(this)

        // Tab click listeners
        binding.btnDue.setOnClickListener {
            if (currentTab != TabType.DUE) {
                currentTab = TabType.DUE
                updateTabUI()
                loadDueData()
            }
        }

        binding.btnReceived.setOnClickListener {
            if (currentTab != TabType.RECEIVED) {
                currentTab = TabType.RECEIVED
                updateTabUI()
                loadReceivedData()
            }
        }

        // Initial load
        updateTabUI()
        loadDueData()
    }

    private fun updateTabUI() {
        if (currentTab == TabType.DUE) {
            binding.btnDue.setBackgroundResource(R.drawable.bg_tab_selector)
            binding.btnDue.setTextColor(Color.BLACK)
            binding.btnReceived.setBackgroundResource(R.drawable.bg_tab_unselected)
            binding.btnReceived.setTextColor(Color.WHITE)
            getDueStats()

        } else {
            binding.btnDue.setBackgroundResource(R.drawable.bg_tab_unselected)
            binding.btnDue.setTextColor(Color.WHITE)
            binding.btnReceived.setBackgroundResource(R.drawable.bg_tab_selector)
            binding.btnReceived.setTextColor(Color.BLACK)
            getReceivedStats()
        }
    }


private fun loadDueData() {

    adapter = StudentPaymentAdapter(null)
    binding.recyclerViewPayments.adapter = adapter
}

private fun loadReceivedData() {

    adapter = StudentPaymentAdapter(null)
    binding.recyclerViewPayments.adapter = adapter
}

private fun setupChart(entries: List<PieEntry>, centerText: String) {
    val dataSet = PieDataSet(entries, "")
    dataSet.colors = listOf(Color.GREEN, Color.RED, Color.BLUE, Color.CYAN, Color.MAGENTA)
    dataSet.valueTextSize = 14f

    val pieData = PieData(dataSet)
    binding.pieChart.data = pieData
    binding.pieChart.centerText = centerText
    binding.pieChart.setCenterTextSize(18f)
    binding.pieChart.description.isEnabled = false
    binding.pieChart.animateY(500)
    binding.pieChart.invalidate()
}

    private fun getReceivedStats() {
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
        viewModel.getReceivedStats(schoolData!!.id).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                if (it.data != null) {


                    loadReceivedDateChart(it.data.statics)

                    if (!it.data.students.isNullOrEmpty()) {
                        binding.tvNoData.visibility = View.GONE
                        students.clear()
                        students.addAll(it.data.students)
                        adapter?.updateData(it.data.students)
                    } else {
                        binding.tvNoData.visibility = View.VISIBLE
                        adapter?.updateData(ArrayList()) // clear adapter if no data
                    }
                }

            }else {
                if (!it.note.isNullOrEmpty()) {
                    binding.tvNoData.visibility=View.VISIBLE
                    Constants.showValidationDialog(this, it.note)
                }
            }

        })
    }

    private fun loadReceivedDateChart(statics: ReceivedStatics) {
        val entries = mutableListOf<PieEntry>()

        val cash = statics.cash?.replace("%", "")?.toFloatOrNull() ?: 0f
        val mobileMoney = statics.mobile_money?.replace("%", "")?.toFloatOrNull() ?: 0f
        val bankTransfer = statics.bank_transfer?.replace("%", "")?.toFloatOrNull() ?: 0f

        if (cash > 0f) entries.add(PieEntry(cash, "Cash"))
        if (mobileMoney > 0f) entries.add(PieEntry(mobileMoney, "Mobile Money"))
        if (bankTransfer > 0f) entries.add(PieEntry(bankTransfer, "Bank Transfer"))

        val centerText = getString(R.string.received_by_mode)

        if (entries.isNotEmpty()) {
            setupChart(entries, centerText)
        } else {
            binding.pieChart.clear()
            binding.pieChart.invalidate()
            binding.pieChart.setNoDataText(getString(R.string.no_data_available)) // optional
        }
    }


    private fun getDueStats() {
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
        viewModel.getDueStats(schoolData!!.id).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                if (it.data!=null)
                {

                    binding.tvNoData.visibility=View.GONE
                    students.clear()
                    students.addAll(it.data.students)
                    adapter!!.updateData(it.data.students)
                    loadDueDateChart(it.data.statics)
                }

            }else {
                if (!it.note.isNullOrEmpty()) {
                    binding.tvNoData.visibility=View.VISIBLE
                    Constants.showValidationDialog(this, it.note)
                }
            }

        })
    }

    private fun loadDueDateChart(statics: DueStatics) {
        val entries = mutableListOf<PieEntry>()

        val paidPercent = statics.Paid.replace("%", "").toFloatOrNull() ?: 0f
        val unpaidPercent = statics.Unpaid.replace("%", "").toFloatOrNull() ?: 0f

        if (paidPercent > 0f) {
            entries.add(PieEntry(paidPercent, "Paid"))
        }
        if (unpaidPercent > 0f) {
            entries.add(PieEntry(unpaidPercent, "Unpaid"))
        }

        setupChart(entries, "Payment Due")
    }
}

