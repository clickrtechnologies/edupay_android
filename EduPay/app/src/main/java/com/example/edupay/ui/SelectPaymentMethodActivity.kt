package com.example.edupay.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.conatctsync.app.EduPay
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.adapter.SearchableAdapter
import com.example.edupay.adapter.SearchableApiAdapter
import com.example.edupay.adapter.StudentFeeApiAdapter
import com.example.edupay.databinding.ActivityParentPaymentBinding
import com.example.edupay.databinding.ActivitySelectPaymentMethodBinding
import com.example.edupay.model.parent_register.Parent
import com.example.edupay.model.payment.PaymentParentRequest
import com.example.edupay.model.payment.partialPayment.PartialPaymentRequest
import com.example.edupay.model.regstudents.ParentIdRequest
import com.example.edupay.model.regstudents.StudentReg
import com.example.edupay.model.school_register.School
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectPaymentMethodActivity : BaseActivity() {


    private lateinit var binding: ActivitySelectPaymentMethodBinding


    private val viewModel: HomeViewModel by viewModels()
    var myTinyDB: MyTinyDB? = null
    var preferenceHelper: PreferenceHelper? = null
    private var parentData: Parent? = null
    private var paymentDataRequest: PaymentParentRequest? = null
    private var payStudent: StudentReg? = null

    // Sample data
    private var SCHOOL_ID: Int = 0

    private var institute: String = ""
    private var branch: String = ""
    private var regNumber: String = ""
    private var selectedTerm: String = ""
    private var batch: String = ""
    private var isPartialpayment: Boolean = false

    private var totalFees: Double = 0.0
    private var termFees: Double = 0.0
    private var uniformFees: Double = 0.0
    private var stationaryFees: Double = 0.0
    private var transportationFees: Double = 0.0
    private var outStandingFees: Double = 0.0
    private var paid_amount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_payment)
        binding = ActivitySelectPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        parentData = myTinyDB!!.getParentData(Constants.PARENT_DATA)
        paymentDataRequest = myTinyDB!!.getPaymentData(Constants.PAYMENT_DATA_REQUEST)
        payStudent = myTinyDB!!.getPayStudentData(Constants.PAY_STUDENT_DATA)
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        binding.ivLogo.startAnimation(animation)

        SCHOOL_ID = intent.getIntExtra("SCHOOL_ID", 0)
        institute = intent.getStringExtra("INSTITUTE") ?: ""
        branch = intent.getStringExtra("BRANCH") ?: ""
        regNumber = intent.getStringExtra("REG_NUMBER") ?: ""
        totalFees = intent.getDoubleExtra("TOTAL_FEES", 0.0)
        selectedTerm = intent.getStringExtra("SELECTED_TERM") ?: ""
        batch = intent.getStringExtra("BATCH") ?: ""
        termFees = intent.getDoubleExtra("TERM_FEES", 0.0)
        uniformFees = intent.getDoubleExtra("UNIFORM_FEES", 0.0)
        stationaryFees = intent.getDoubleExtra("STATIONARY_FEES", 0.0)
        transportationFees = intent.getDoubleExtra("TRANSPORTATION_FEES", 0.0)
        outStandingFees = intent.getDoubleExtra("OUTSTANDING_FEES", 0.0)
        paid_amount = intent.getDoubleExtra("PAID_AMOUNT", 0.0)
        isPartialpayment = intent.getBooleanExtra("IS_FROM_PARTIAL_PAYMENT", false)


        val methods = arrayOf(
            "Select Payment Method",
            "Bank Transfer",
            "Via PIN",
            "Via CRDB Reference Number",
            "Mobile Money Account"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, methods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPaymentMethod.adapter = adapter

        binding.spinnerPaymentMethod.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    binding.layoutBankDetails.visibility = View.GONE
                    binding.layoutPin.visibility = View.GONE
                    binding.layoutCrdbRef.visibility = View.GONE
                    binding.layoutMobileMoney.visibility = View.GONE

                    when (methods[position]) {
                        "Bank Transfer" -> binding.layoutBankDetails.visibility = View.VISIBLE
                        "Via PIN" -> binding.layoutPin.visibility = View.VISIBLE
                        "Via CRDB Reference Number" -> binding.layoutCrdbRef.visibility =
                            View.VISIBLE

                        "Mobile Money Account" -> binding.layoutMobileMoney.visibility =
                            View.VISIBLE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}

            }

        binding.btnPayNow.setOnClickListener {
            if (validateForm()) {
                if (isPartialpayment) {
                    savePartialPaymentDetail()
                } else {
                    savePaymentDetail()
                }
                // Form is valid, proceed with API/payment logic
                Toast.makeText(this, "Payment initiated...", Toast.LENGTH_SHORT).show()

                // TODO: Add real payment logic here
            }
        }
    }

    private fun validateForm(): Boolean {
        val selectedMethod = binding.spinnerPaymentMethod.selectedItem.toString()

        return when (selectedMethod) {
            "Bank Transfer" -> {
                val bankName = binding.etBankName.text.toString().trim()
                val accountNumber = binding.etAccountNumber.text.toString().trim()

                when {
                    bankName.isEmpty() -> {
                        showValidationDialog(getString(R.string.bank_name_required))
                        false
                    }

                    accountNumber.isEmpty() -> {
                        showValidationDialog(getString(R.string.account_number_required))
                        false
                    }

                    accountNumber.length < 6 -> {
                        showValidationDialog(getString(R.string.account_number_too_short))
                        false
                    }

                    else -> true
                }
            }

            "Via PIN" -> {
                val pin = binding.etPin.text.toString().trim()

                when {
                    pin.isEmpty() -> {
                        showValidationDialog(getString(R.string.pin_required))
                        false
                    }

                    pin.length < 4 -> {
                        showValidationDialog(getString(R.string.pin_too_short))
                        false
                    }

                    else -> true
                }
            }

            "Via CRDB Reference Number" -> {
                val crdbRef = binding.etCrdbRef.text.toString().trim()
                if (crdbRef.isEmpty()) {
                    showValidationDialog(getString(R.string.crdb_ref_required))
                    false
                } else true
            }

            "Mobile Money Account" -> {
                val mobileMoney = binding.etMobileMoney.text.toString().trim()

                when {
                    mobileMoney.isEmpty() -> {
                        showValidationDialog(getString(R.string.mobile_money_required))
                        false
                    }

                    mobileMoney.length < 10 -> {
                        showValidationDialog(getString(R.string.mobile_money_invalid))
                        false
                    }

                    else -> true
                }
            }

            else -> {
                showValidationDialog(getString(R.string.please_select_payment_method))
                false
            }
        }
    }

    private fun showValidationDialog(message: String) {
        Dialogs.dialogWithCloseButton(
            activity = this@SelectPaymentMethodActivity,
            title = getString(R.string.message),
            msg = message,
            btntext = getString(R.string.ok),
            cancelable = true
        )
    }


    private fun savePaymentDetail() {

        if (paymentDataRequest == null) {
            return
        }
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
        viewModel.savePaymentDetail(SCHOOL_ID, paymentDataRequest!!).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {
                it.profile?.let { data ->
                    val intent = Intent(this, ThankYouActivity::class.java).apply {
                        putExtra("TOTAL_FEES", totalFees)
                        putExtra("SELECTED_TERM", selectedTerm)
                        putExtra("INSTITUTE", institute)
                        putExtra("BRANCH", branch)
                        putExtra("BATCH", batch)
                        putExtra("REG_NUMBER", regNumber)
                        putExtra("TERM_FEES", termFees)
                        putExtra("UNIFORM_FEES", uniformFees)
                        putExtra("STATIONARY_FEES", stationaryFees)
                        putExtra("TRANSPORTATION_FEES", transportationFees)
                    }
                    startActivity(intent)
                    finish()
                }
            } else {

            }

        })
    }

    private fun savePartialPaymentDetail() {

        if (paymentDataRequest == null) {
            return
        }


        val partialPaymentRequest = paymentDataRequest!!.toPartialPaymentRequest(
            outstandingAmount = outStandingFees,
            paidAmount = paid_amount,
            schoolId = SCHOOL_ID,
            termFees = termFees
        )
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

        viewModel.savePartialPaymentDetail( partialPaymentRequest)
            .observe(this, Observer {
                dismissProgressDialog()
                if (it.message.equals(Constants.SUCCESS)) {
                    val intent = Intent(this, ThankYouActivity::class.java).apply {
                        putExtra("TOTAL_FEES", totalFees)
                        putExtra("SELECTED_TERM", selectedTerm)
                        putExtra("INSTITUTE", institute)
                        putExtra("BRANCH", branch)
                        putExtra("BATCH", batch)
                        putExtra("REG_NUMBER", regNumber)
                        putExtra("TERM_FEES", termFees)
                        putExtra("UNIFORM_FEES", uniformFees)
                        putExtra("STATIONARY_FEES", stationaryFees)
                        putExtra("TRANSPORTATION_FEES", transportationFees)
                    }
                    startActivity(intent)
                    finish()

                } else {

                }

            })
    }


    fun PaymentParentRequest.toPartialPaymentRequest(
        outstandingAmount: Double,
        paidAmount: Double,
        schoolId: Int,
        termFees: Double
    ): PartialPaymentRequest {
        return PartialPaymentRequest(
            feesPaid = this.feesPaid,
            outstandingAmount = outstandingAmount,
            paidAmount = paidAmount,
            parentId = this.parentId,
            registrationNumber = this.registrationNumber,
            schoolId = schoolId,
            schoolUniformFees = this.schoolUniformFees,
            schoolUniformFeesPaid = this.schoolUniformFeesPaid,
            studentId = this.studentId,
            studentStationaryFees = this.studentStationaryFees,
            studentStationaryFeesPaid = this.studentStationaryFeesPaid,
            term = this.term,
            termFees = termFees,
            transportionFees = this.transportionFees,
            transportionFeesPaid = this.transportionFeesPaid
        )
    }
}