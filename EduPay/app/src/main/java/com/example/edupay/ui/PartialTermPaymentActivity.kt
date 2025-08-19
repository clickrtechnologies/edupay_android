package com.example.edupay.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.app.conatctsync.app.EduPay
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.databinding.ActivityMainBinding
import com.example.edupay.databinding.ActivityPartialPaymentBinding
import com.example.edupay.model.payment.PaymentParentRequest
import com.example.edupay.model.school_register.RegisterSchoolRequest
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PartialTermPaymentActivity : BaseActivity() {
    private var outstandingAmount: Double=0.0
    private var outstandingAmount_v1: Double=0.0
    private lateinit var binding: ActivityPartialPaymentBinding
    private val viewModel: HomeViewModel by viewModels()
    var myTinyDB: MyTinyDB? = null
    var preferenceHelper: PreferenceHelper? = null
    private var actualAmountDue: Double = 0.0
    private var termName: String? = null
    private var termFees: Double = 0.0
    private var uniformFees: Double = 0.0
    private var stationaryFees: Double = 0.0
    private var transportationFees: Double = 0.0
    private var schoolId: Int? = 0
    private var parentId: Int? = 0
    private var studentId: Int? = 0
    private var paymentDataRequest: PaymentParentRequest? = null
    private var institute: String=""
    private var regNumber: String=""
    private var branch: String=""
    private var batch: String=""
    private var totalFees = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPartialPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        // ✅ Get intent extras
        termName = intent.getStringExtra("TERM_NAME")
        termFees = intent.getDoubleExtra("TERM_FEES", 0.0)
        uniformFees = intent.getDoubleExtra("UNIFORM_FEES", 0.0)
        stationaryFees = intent.getDoubleExtra("STATIONARY_FEES", 0.0)
        transportationFees = intent.getDoubleExtra("TRANSPORTATION_FEES", 0.0)
        schoolId = intent.getIntExtra("SCHOOL_ID",0)
        parentId = intent.getIntExtra("PARENT_ID",0)
        studentId = intent.getIntExtra("STUDENT_ID",0)
        // Simulate getting actual amount due (you should replace it with actual value)
        actualAmountDue = intent.getDoubleExtra("actual_amount_due", 0.0)


        institute = intent.getStringExtra("INSTITUTE") ?: ""
        branch = intent.getStringExtra("BRANCH") ?: ""
        regNumber = intent.getStringExtra("REG_NUMBER") ?: ""
        totalFees = intent.getDoubleExtra("TOTAL_FEES", 0.0)
     //   selectedTerm = intent.getStringExtra("SELECTED_TERM") ?: ""
        batch = intent.getStringExtra("BATCH") ?: ""


        binding.tvAmountDue.text = getString(R.string.amount_due_formatted, termFees.toString())
        binding.tvInstitute.text = "School: $institute"
        binding.tvRegNumber.text = "Reg Number: $regNumber"
        updateOutstandingAmount()
        // ✅ Set header and amount info
      //  binding.tvHeader.text = termName
       // binding.tvTotalAmount.text = getString(R.string.rupee_symbol_with_amount, termFees)
        paymentDataRequest = myTinyDB!!.getPaymentData(Constants.PAYMENT_DATA_REQUEST)

        binding.btnChoosePayment.setOnClickListener {

            val amountStr = binding.etPartialAmount.text.toString().trim()
            val amount = amountStr.toDoubleOrNull()

            if (amountStr.isEmpty()) {
                Constants.showValidationDialog(this, getString(R.string.enter_amount_valid))
                return@setOnClickListener
            }

            if (amount == null || amount <= 0) {
                Constants.showValidationDialog(this, getString(R.string.enter_valid_amount))
                return@setOnClickListener
            }

            if (amount < 100) {
                Constants.showValidationDialog(this, getString(R.string.enter_minimum_100))
                return@setOnClickListener
            }

            if (amount > termFees) {
                Constants.showValidationDialog(this, getString(R.string.amount_should_not_exceed_term_fees))
                return@setOnClickListener
            }

        /*    val intentvalue = Intent(this, StudentPaymentActivity::class.java).apply {
                putExtra("TOTAL_FEES", totalFees)
                //    putExtra("SELECTED_TERM", binding.tvSelectedTerm.text.toString())
                putExtra("INSTITUTE", institute)
                putExtra("BRANCH", branch)
                putExtra("BATCH", batch)
                putExtra("REG_NUMBER", regNumber)
                putExtra("TERM_FEES", currentTermFees)
                putExtra("UNIFORM_FEES", uniformFees)
                putExtra("STATIONARY_FEES", stationaryFees)
                putExtra("TRANSPORTATION_FEES", transportationFees)
                putExtra("SCHOOL_ID", SCHOOL_ID)
            }*/

         /*   // ✅ Proceed with payment
            val paymentIntent = Intent(this, StudentPaymentActivity::class.java).apply {
                putExtra("AMOUNT", amount)
                putExtra("TERM_NAME", termName)
                putExtra("SCHOOL_ID", schoolId)
                putExtra("PARENT_ID", parentId)
                putExtra("STUDENT_ID", studentId)
                putExtra("UNIFORM_FEES", uniformFees)
                putExtra("STATIONARY_FEES", stationaryFees)
                putExtra("TRANSPORTATION_FEES", transportationFees)
            }
            startActivity(paymentIntent)*/
val paidAmount=binding.etPartialAmount.text.toString()
            val intentvalue = Intent(this, StudentPaymentActivity::class.java).apply {
                putExtra("TOTAL_FEES", totalFees)
                //    putExtra("SELECTED_TERM", binding.tvSelectedTerm.text.toString())
                putExtra("INSTITUTE", institute)
                putExtra("BRANCH", branch)
                putExtra("BATCH", batch)
                putExtra("REG_NUMBER", regNumber)
                putExtra("TERM_FEES", termFees)
                putExtra("UNIFORM_FEES", uniformFees)
                putExtra("STATIONARY_FEES", stationaryFees)
                putExtra("TRANSPORTATION_FEES", transportationFees)
                putExtra("OUTSTANDING_FEES", outstandingAmount_v1)
                putExtra("PAID_AMOUNT", paidAmount.toDoubleOrNull())
                putExtra("SCHOOL_ID", schoolId)
            }
            startActivity(intentvalue)
        }



    }

    private fun updateOutstandingAmount() {
        binding.etPartialAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().trim()
                val enteredAmount = input.toDoubleOrNull()

                if (input.isEmpty()) {
                    binding.tvOutstandingAmount.text = ""
                    return
                }

                if (enteredAmount == null || enteredAmount <= 0) {
                    binding.tvOutstandingAmount.text = getString(R.string.enter_valid_amount)
                    return
                }

                if (enteredAmount > termFees) {
                    binding.tvOutstandingAmount.text = getString(R.string.invalid_amount_exceeds_term_fees)
                } else if (enteredAmount < 100) {
                    binding.tvOutstandingAmount.text = getString(R.string.enter_minimum_100)
                } else {
                     outstandingAmount = termFees - enteredAmount
                    binding.tvOutstandingAmount.text = getString(R.string.outstanding_amount_formatted, outstandingAmount)
                    outstandingAmount_v1 = termFees - enteredAmount
                }
            }
        })

    }

}