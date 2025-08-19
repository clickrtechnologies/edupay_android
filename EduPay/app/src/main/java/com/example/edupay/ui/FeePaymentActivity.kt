package com.example.edupay.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.conatctsync.app.EduPay
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.adapter.SearchableAdapter
import com.example.edupay.databinding.ActivityFeePaymentBinding
import com.example.edupay.model.parent_register.Parent
import com.example.edupay.model.payment.PaymentParentRequest
import com.example.edupay.model.student_fees_data.FeesProfile
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeePaymentActivity : BaseActivity() {


    private var institute: String=""
    private var regNumber: String=""
    private var branch: String=""
    private var batch: String=""
    private var STUDENT_ID: Int=0
    private var PARENT_ID: Int=0
    private var SCHOOL_ID: Int=0
    private var REGISTRATION_NUMBER: String=""
    private var isUniformSelected = false
    private var isStationarySelected = false
    private var isTransportSelected = false

    private lateinit var binding: ActivityFeePaymentBinding
    private var parentData: Parent?=null
    var myTinyDB: MyTinyDB? = null
    var preferenceHelper: PreferenceHelper? = null

    private val viewModel: HomeViewModel by viewModels()
    // Data binding

    // Sample data

    // Fee structure
//    private val termFees = mapOf(
//        "Term 1" to 500,
//        "Term 2" to 600,
//        "Term 3" to 700
//    )

 /*   private var termFees = mapOf(
        R.id.cbTerm1 to 100,
        R.id.cbTerm2 to 120,
        R.id.cbTerm3 to 130
    )*/

    private lateinit var termCheckBoxMap: Map<Int, CheckBox>

    private var termFees: MutableMap<Int, Double> = mutableMapOf()

    private var uniformFees = 50.0
    private var stationaryFees = 30.0
    private var transportationFees = 100.0

    private var currentTermFees = 0.0
    private var totalFees = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_payment)
        binding = ActivityFeePaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        parentData=myTinyDB!!.getParentData(Constants.PARENT_DATA)
        preferenceHelper = PreferenceHelper(this)
        termCheckBoxMap = mapOf(
            0 to binding.cbTerm1,
            1 to binding.cbTerm2,
            2 to binding.cbTerm3
        )
        setupClickListeners()
         institute = intent.getStringExtra("INSTITUTE") ?: ""
         branch = intent.getStringExtra("BRANCH") ?: ""
         regNumber = intent.getStringExtra("REG_NUMBER") ?: ""
        batch = intent.getStringExtra("BATCH") ?: ""
        STUDENT_ID = intent.getIntExtra("STUDENT_ID",0)
        PARENT_ID = intent.getIntExtra("PARENT_ID",0)
        SCHOOL_ID = intent.getIntExtra("SCHOOL_ID",0)
       // SCHOOL_ID=72
       // PARENT_ID=42
       // STUDENT_ID=12
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        binding.ivLogo.startAnimation(animation)
        getStudentProfileRecord(SCHOOL_ID,PARENT_ID,STUDENT_ID)
    }

    private fun setupClickListeners() {

        // Term Selection
        setupTermCheckBoxes()
 /*       binding.tvSelectedTerm.setOnClickListener {
            showTermSelectionBottomSheet { selectedTerm ->
                binding.tvSelectedTerm.text = selectedTerm
                binding.tvSelectedTerm.setTextColor(ContextCompat.getColor(this, R.color.black))

                currentTermFees = termFees[selectedTerm] ?: 0
                binding.tvTermFees.text = "Term Fees: USD $currentTermFees"
                binding.tvTermFees.visibility = View.VISIBLE

                // Enable optional fees checkboxes
                binding.cbUniform.isEnabled = true
                binding.cbStationary.isEnabled = true
                binding.cbTransportation.isEnabled = true

                calculateTotalFees()
            }
        }*/

        // Optional fees selection listeners
        binding.cbUniform.setOnCheckedChangeListener { _, isChecked ->
            calculateTotalFees()
        }


        binding.cbStationary.setOnCheckedChangeListener { _, isChecked ->
            calculateTotalFees()
        }

        binding.cbTransportation.setOnCheckedChangeListener { _, isChecked ->
            calculateTotalFees()
        }
        // Pay button
      /*  binding.btnPay.setOnClickListener {
            val selectedTermIds = termCheckBoxMap.filter { it.value.isChecked }.keys.toList()


            if (selectedTermIds.size == 1) {
                val termCheckBox = termCheckBoxMap[selectedTermIds.first()]
                val selectedTermText = termCheckBox?.text.toString()
                val fullTermAmount = termFees[termCheckBox?.id] ?: 0.0



                val intent = Intent(this, PartialTermPaymentActivity::class.java).apply {
                    putExtra("TERM_NAME", selectedTermText)
                    putExtra("TERM_FEES", fullTermAmount)
                    putExtra("UNIFORM_FEES", if (binding.cbUniform.isChecked) uniformFees else 0.0)
                    putExtra("STATIONARY_FEES", if (binding.cbStationary.isChecked) stationaryFees else 0.0)
                    putExtra("TRANSPORTATION_FEES", if (binding.cbTransportation.isChecked) transportationFees else 0.0)
                    putExtra("SCHOOL_ID", SCHOOL_ID)
                    putExtra("PARENT_ID", PARENT_ID)
                    putExtra("STUDENT_ID", STUDENT_ID)
                }
                startActivity(intent)
            } else {
                val selectedTerms = mutableListOf<String>()
                if (binding.cbTerm1.isChecked) selectedTerms.add("Term 1")
                if (binding.cbTerm2.isChecked) selectedTerms.add("Term 2")
                if (binding.cbTerm3.isChecked) selectedTerms.add("Term 3")
                // Create the request
                val paymentRequest = PaymentParentRequest(
                    feesPaid = true,
                    parentId = PARENT_ID,
                    registrationNumber = regNumber,
                    schoolUniformFees = if (binding.cbUniform.isChecked) uniformFees else 0.0,
                    studentId = STUDENT_ID,
                    studentStationaryFees = if (binding.cbStationary.isChecked) stationaryFees else 0.0,
                    transportionFees = if (binding.cbTransportation.isChecked) transportationFees else 0.0,
                    term = selectedTerms.joinToString(","),
                    transportionFeesPaid = isTransportSelected,
                    studentStationaryFeesPaid = isStationarySelected,
                    schoolUniformFeesPaid = isUniformSelected,

                    // Assuming payment hasn't been processed yet
                )
                myTinyDB!!.putPaymentData(Constants.PAYMENT_DATA_REQUEST,paymentRequest)
                val intentvalue = Intent(this, StudentPaymentActivity::class.java).apply {
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
                }
                startActivity(intentvalue)
            }






        }*/

        // Pay button
        binding.btnPay.setOnClickListener {

            val selectedTermIds = termCheckBoxMap.filter { it.value.isChecked }.keys.toList()

            // Collect selected terms
            val selectedTerms = mutableListOf<String>().apply {
                if (binding.cbTerm1.isChecked) add("Term 1")
                if (binding.cbTerm2.isChecked) add("Term 2")
                if (binding.cbTerm3.isChecked) add("Term 3")
            }

            // Save payment request to TinyDB
            val paymentRequest = PaymentParentRequest(
                feesPaid = true,
                parentId = PARENT_ID,
                registrationNumber = regNumber,
                schoolUniformFees = if (binding.cbUniform.isChecked) uniformFees else 0.0,
                studentId = STUDENT_ID,
                studentStationaryFees = if (binding.cbStationary.isChecked) stationaryFees else 0.0,
                transportionFees = if (binding.cbTransportation.isChecked) transportationFees else 0.0,
                term = selectedTerms.joinToString(","),
                transportionFeesPaid = isTransportSelected,
                studentStationaryFeesPaid = isStationarySelected,
                schoolUniformFeesPaid = isUniformSelected
            )
            myTinyDB!!.putPaymentData(Constants.PAYMENT_DATA_REQUEST, paymentRequest)

            // Determine activity and extras
            val targetActivity: Class<*> = when {
                selectedTermIds.size == 1 -> {
                    if (binding.cbPartialPayment.isChecked) {
                        PartialTermPaymentActivity::class.java
                    } else {
                        StudentPaymentActivity::class.java
                    }
                }
                else -> StudentPaymentActivity::class.java
            }

            // Get term details if only one is selected
            val termCheckBox = termCheckBoxMap[selectedTermIds.firstOrNull()]
            val fullTermAmount = termFees[termCheckBox?.id] ?: 0.0

            // Create intent with common extras
            val intent = Intent(this, targetActivity).apply {
                putExtra("TOTAL_FEES", totalFees)
                putExtra("INSTITUTE", institute)
                putExtra("BRANCH", branch)
                putExtra("BATCH", batch)
                putExtra("REG_NUMBER", regNumber)
                putExtra("TERM_FEES", if (selectedTermIds.size == 1) fullTermAmount else currentTermFees)
                putExtra("UNIFORM_FEES", if (binding.cbUniform.isChecked) uniformFees else 0.0)
                putExtra("STATIONARY_FEES", if (binding.cbStationary.isChecked) stationaryFees else 0.0)
                putExtra("TRANSPORTATION_FEES", if (binding.cbTransportation.isChecked) transportationFees else 0.0)
                putExtra("SCHOOL_ID", SCHOOL_ID)
                putExtra("PARENT_ID", PARENT_ID)
                putExtra("STUDENT_ID", STUDENT_ID)
            }

            startActivity(intent)
        }

    }
    private fun calculateTotalFees() {
        isUniformSelected = binding.cbUniform.isChecked
        isStationarySelected = binding.cbStationary.isChecked
        isTransportSelected = binding.cbTransportation.isChecked
        totalFees = currentTermFees

        if (binding.cbUniform.isChecked) {
            totalFees += uniformFees
        }

        if (binding.cbStationary.isChecked) {
            totalFees += stationaryFees
        }

        if (binding.cbTransportation.isChecked) {
            totalFees += transportationFees
        }
        // Show or hide partial payment checkbox based on term selections
        val termCheckedCount = listOf(
            binding.cbTerm1.isChecked,
            binding.cbTerm2.isChecked,
            binding.cbTerm3.isChecked
        ).count { it }

        if (termCheckedCount == 1) {
            binding.cbPartialPayment.visibility = View.VISIBLE
        } else {
            binding.cbPartialPayment.apply {
                visibility = View.GONE
                isChecked = false
            }
        }

        binding.tvTotalFees.text = "Total: $ $totalFees"
        binding.btnPay.isEnabled = currentTermFees > 0
    }



    private fun setupTermCheckBoxes() {
        val checkBoxes = listOf(binding.cbTerm1, binding.cbTerm2, binding.cbTerm3)

        val listener = CompoundButton.OnCheckedChangeListener { _, _ ->
            var total = 0.0
            var selectedCount = 0
            for (checkBox in checkBoxes) {
                if (checkBox.isChecked) {
                    total += termFees[checkBox.id] ?: 0.0
                    selectedCount++
                }
            }

            currentTermFees = total
            binding.tvTermFees.text = "Term Fees: $ $currentTermFees"

            val hasSelection = selectedCount > 0
            binding.cbUniform.isEnabled = hasSelection
            binding.cbStationary.isEnabled = hasSelection
            binding.cbTransportation.isEnabled = hasSelection
            // Show partial payment notice only if exactly one term is selected
            binding.tvPartialNotice.visibility = if (selectedCount == 1) View.VISIBLE else View.GONE
            binding.cbPartialPayment.visibility = if (selectedCount == 1) View.VISIBLE else View.GONE

            calculateTotalFees()
        }

        checkBoxes.forEach { it.setOnCheckedChangeListener(listener) }
    }


    /*
        private fun showTermSelectionBottomSheet(onTermSelected: (String) -> Unit) {
            val bottomSheet = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_term_selection, null)

            val recyclerView = view.findViewById<RecyclerView>(R.id.rvTerms)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = TermAdapter(listOf("Term 1", "Term 2", "Term 3")) { term ->
                onTermSelected(term)
                bottomSheet.dismiss()
            }

            bottomSheet.setContentView(view)
            bottomSheet.show()
        }
    */

    private fun showTermSelectionBottomSheet(
        onItemSelected: (String) -> Unit
    ) {
        val bottomSheet: BottomSheetDialog = BottomSheetDialog(this)
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_term, null)

        val recyclerTerm: RecyclerView = view.findViewById(R.id.recyclerTerm)
       val items = listOf("Term 1", "Term 2", "Term 3")
        val adapter = SearchableAdapter(items) { selectedItem: String ->
            onItemSelected(selectedItem)
            bottomSheet.dismiss()
        }

        recyclerTerm.layoutManager = LinearLayoutManager(this)
        recyclerTerm.adapter = adapter

        bottomSheet.setContentView(view)
        bottomSheet.show()
    }

    private fun getStudentProfileRecord(SCHOOL_ID: Int, PARENT_ID: Int, STUDENT_ID: Int) {
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
        viewModel.getStudentProfileRecord(SCHOOL_ID,PARENT_ID,STUDENT_ID).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {
                println("Api Success response::"+it)
                val defaultProfile = it.profile!!.first() // Use first item
// Set dynamic prices

                uniformFees = defaultProfile.schoolUniformFees
                stationaryFees = defaultProfile.studentStationaryFees
                transportationFees = defaultProfile.transportionFees
                binding.txtUniform.text = "Include School Uniform ($ ${defaultProfile.schoolUniformFees})"
                binding.txtStationary.text = "Include Stationary ($ ${defaultProfile.studentStationaryFees})"
                binding.txtTransportation.text = "Include Transportation ($ ${defaultProfile.transportionFees})"
                bindFeesToCheckBoxes(it.profile)
            }

        })
    }



    fun bindFeesToCheckBoxes(profileList: List<FeesProfile>) {
        termFees.clear()

        profileList.forEachIndexed { index, profile ->
            val checkBox = termCheckBoxMap[index]
            checkBox?.apply {
                text = "${profile.term} ($ ${profile.termFees})"
                tag = profile.termFees
                visibility = View.VISIBLE
            }
            termFees[checkBox?.id ?: return@forEachIndexed] = profile.termFees
        }

        setupTermCheckBoxes() // after termFees is ready
    }
}