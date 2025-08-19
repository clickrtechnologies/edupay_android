package com.example.edupay.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import androidx.activity.viewModels
import com.app.conatctsync.app.EduPay
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.databinding.ActivityFeePaymentv1Binding
import com.example.edupay.model.parent_register.Parent
import com.example.edupay.model.student_fees_data.FeesProfile
import com.example.edupay.model.student_fees_data.SelectableFeesProfile
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeePaymentActivityV1 : BaseActivity() {

    private lateinit var binding: ActivityFeePaymentv1Binding
    private val viewModel: HomeViewModel by viewModels()
    private var parentData: Parent? = null
    private var myTinyDB: MyTinyDB? = null
    private var preferenceHelper: PreferenceHelper? = null

    private var selectedTerms = mutableListOf<SelectableFeesProfile>()
    private var institute = ""
    private var regNumber = ""
    private var branch = ""
    private var batch = ""
    private var STUDENT_ID = 0
    private var PARENT_ID = 0
    private var SCHOOL_ID = 0
    private var totalFees = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeePaymentv1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        myTinyDB = MyTinyDB(this)
        parentData = myTinyDB!!.getParentData(Constants.PARENT_DATA)
        preferenceHelper = PreferenceHelper(this)

        // Extract intent data
        institute = intent.getStringExtra("INSTITUTE") ?: ""
        branch = intent.getStringExtra("BRANCH") ?: ""
        regNumber = intent.getStringExtra("REG_NUMBER") ?: ""
        batch = intent.getStringExtra("BATCH") ?: ""
        STUDENT_ID = intent.getIntExtra("STUDENT_ID", 0)
        PARENT_ID = intent.getIntExtra("PARENT_ID", 0)
        SCHOOL_ID = intent.getIntExtra("SCHOOL_ID", 0)

        // Animation
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        binding.ivLogo.startAnimation(animation)

        // Load data
        getStudentProfileRecord(SCHOOL_ID, PARENT_ID, STUDENT_ID)

        // Payment button
        binding.btnPay.setOnClickListener {
            val intent = Intent(this, StudentPaymentActivity::class.java).apply {
                putExtra("TOTAL_FEES", totalFees)
                putExtra("INSTITUTE", institute)
                putExtra("BRANCH", branch)
                putExtra("BATCH", batch)
                putExtra("REG_NUMBER", regNumber)
            }
            startActivity(intent)
        }
    }

    private fun getStudentProfileRecord(schoolId: Int, parentId: Int, studentId: Int) {
        if (!EduPay.hasNetwork()) {
            Dialogs.dialogWithCloseButton(
                activity = this,
                title = getString(R.string.network_title),
                msg = getString(R.string.network_error),
                btntext = getString(R.string.ok),
                cancelable = true
            )
            return
        }


        showProgressDialog()
        viewModel.getStudentProfileRecord(72, 42, 12).observe(this) {
            dismissProgressDialog()
            if (it.message == Constants.SUCCESS && !it.profile.isNullOrEmpty()) {
                setupTermOptions(it.profile)
            }
        }
    }

    private fun setupTermOptions(profileList: List<FeesProfile>) {
        selectedTerms = profileList.map { SelectableFeesProfile(it) }.toMutableList()
        binding.termContainer.removeAllViews()

        for ((index, term) in selectedTerms.withIndex()) {
            val termView = layoutInflater.inflate(R.layout.item_term_selection, binding.termContainer, false)

            val cbTerm = termView.findViewById<CheckBox>(R.id.cbTerm)
            val cbUniform = termView.findViewById<CheckBox>(R.id.cbUniform)
            val cbStationary = termView.findViewById<CheckBox>(R.id.cbStationary)
            val cbTransport = termView.findViewById<CheckBox>(R.id.cbTransport)

            cbTerm.text = "${term.profile.term} (USD ${term.profile.termFees})"
            cbUniform.text = "Include School Uniform (USD ${term.profile.schoolUniformFees})"
            cbStationary.text = "Include Stationary (USD ${term.profile.studentStationaryFees})"
            cbTransport.text = "Include Transportation (USD ${term.profile.transportionFees})"
            cbTerm.setOnCheckedChangeListener { _, isChecked ->
                term.isSelected = isChecked
                cbUniform.isEnabled = isChecked
                cbStationary.isEnabled = isChecked
                cbTransport.isEnabled = isChecked
                calculateTotalFees()
            }

            cbUniform.setOnCheckedChangeListener { _, isChecked ->
                term.includeUniform = isChecked
                calculateTotalFees()
            }

            cbStationary.setOnCheckedChangeListener { _, isChecked ->
                term.includeStationary = isChecked
                calculateTotalFees()
            }

            cbTransport.setOnCheckedChangeListener { _, isChecked ->
                term.includeTransport = isChecked
                calculateTotalFees()
            }

            binding.termContainer.addView(termView)
        }
    }

    private fun calculateTotalFees() {
        totalFees = 0.0
        for (term in selectedTerms) {
            if (term.isSelected) {
                totalFees += term.profile.termFees
                if (term.includeUniform) totalFees += term.profile.schoolUniformFees
                if (term.includeStationary) totalFees += term.profile.studentStationaryFees
                if (term.includeTransport) totalFees += term.profile.transportionFees
            }
        }
        binding.tvTermFees.text = "Term Fees: USD $totalFees"
        binding.tvTotalFees.text = "USD $totalFees"
        binding.btnPay.isEnabled = totalFees > 0
    }
}
