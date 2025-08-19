package com.example.edupay.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
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
import com.example.edupay.model.parent_register.Parent
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity() {


    private var schoolId: Int = 0
    private var STUDENT_ID: Int = 0
    private var STUDENT_NAME: String = ""
    private var PARENT_ID: Int = 0
    private lateinit var binding: ActivityParentPaymentBinding
    private var parentData: Parent? = null


    private val viewModel: HomeViewModel by viewModels()
    var myTinyDB: MyTinyDB? = null
    var preferenceHelper: PreferenceHelper? = null
    var institutesApi: ArrayList<School>? = null
    var avialableBranches: ArrayList<String>? = null
    var avialableSessions: ArrayList<String>? = null
    var avialableStudents: ArrayList<StudentReg>? = null

    // Sample data


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_payment)
        binding = ActivityParentPaymentBinding.inflate(layoutInflater)
        myTinyDB = MyTinyDB(this)
        parentData = myTinyDB!!.getParentData(Constants.PARENT_DATA)

        preferenceHelper = PreferenceHelper(this)
        setContentView(binding.root)
        setupClickListeners()

        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        binding.ivLogo.startAnimation(animation)
        getAlSchoolDetail()
//        getSchoolDetails()
    }

    private fun setupClickListeners() {
        // Institute Selection
        /*      binding.tvInstitute.setOnClickListener {
                  showSearchableBottomSheet(
                      title = "Select Institute",
                      items = institutes.map { it.displayName }
                  ) { selected ->
                      resetDownstreamFields()
                      binding.tvInstitute.text = selected
                      binding.tvInstitute.setTextColor(ContextCompat.getColor(this, R.color.black))
                      binding.branchContainer.visibility = View.VISIBLE

                  }
              }*/
        binding.clInstitute.setOnClickListener {
            showSearchableBottomSheetAPI(
                title = getString(R.string.institute_name),
                items = ArrayList(institutesApi!!) // pass full object list, not just names
            ) { selected ->
                schoolId = selected.id
                resetDownstreamFields()
                binding.tvInstitute.text = selected.name
                binding.tvInstitute.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.branchContainer.visibility = View.VISIBLE
                if (schoolId != 0) {
                    // getAllBranches(schoolId)
                    val parentId = parentData!!.id
                    getSchoolDetails(schoolId, parentId)
                }

            }
        }


        // Branch Selection
        binding.clBranch.setOnClickListener {
            if (binding.tvInstitute.text != getString(R.string.institute_name)) {
                /*   val selectedInstitute =
                       institutesApi!!.first { it.name == binding.tvInstitute.text.toString() }*/

                if (!avialableBranches.isNullOrEmpty()) {
                    showSearchableBottomSheet(
                        title = getString(R.string.branch_name),
                        items = avialableBranches!!.map { it }
                    ) { selected ->
                        binding.tvBranch.text = selected
                        binding.tvBranch.setTextColor(ContextCompat.getColor(this, R.color.black))
                        binding.batchContainer.visibility = View.VISIBLE
                        binding.regNumberContainer.visibility = View.GONE
                    }
                } else {
                    Dialogs.dialogWithCloseButton(
                        activity = this,
                        title = getString(R.string.message),
                        msg = getString(R.string.no_result_found_branch),
                        btntext = getString(R.string.ok),
                        cancelable = true
                    )
                }

            } else {
                Toast.makeText(this, getString(R.string.institute_first), Toast.LENGTH_SHORT).show()
            }
        }

        // Batch Selection
        binding.tvBatch.setOnClickListener {
            if (binding.tvBranch.text != getString(R.string.branch_name)) {

                if (!avialableSessions.isNullOrEmpty()) {
                    val selectedInstitute =
                        institutesApi!!.first { it.name == binding.tvInstitute.text.toString() }
                    showSearchableBottomSheet(
                        title = getString(R.string.branch_name),
                        items = avialableSessions!!
                    ) { selected ->
                        binding.tvBatch.text = selected
                        binding.tvBatch.setTextColor(ContextCompat.getColor(this, R.color.black))
                        // showChildSelection()
                        binding.regNumberContainer.visibility = View.VISIBLE
                    }

                }

            } else {
                Toast.makeText(this, getString(R.string.branch_first), Toast.LENGTH_SHORT).show()
            }
        }

        // Registration Selection
        binding.tvRegNumber.setOnClickListener {
            showChildSelection()
        }

        // Submit Button
        binding.btnSubmit.setOnClickListener {
            submitForm()
        }

    }

    private fun showSearchableBottomSheet(
        title: String,
        items: List<String>,
        onItemSelected: (String) -> Unit
    ) {
        val bottomSheet: BottomSheetDialog = BottomSheetDialog(this)
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_searchable, null)

        val searchField: TextInputEditText = view.findViewById(R.id.searchField)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        val adapter = SearchableAdapter(items) { selectedItem: String ->
            onItemSelected(selectedItem)
            bottomSheet.dismiss()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        bottomSheet.setContentView(view)
        bottomSheet.show()
    }

    private fun showSearchableBottomSheetAPI(
        title: String,
        items: ArrayList<School>,
        onItemSelected: (School) -> Unit
    ) {
        val bottomSheet: BottomSheetDialog = BottomSheetDialog(this)
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_searchable, null)

        val searchField: TextInputEditText = view.findViewById(R.id.searchField)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        val adapter = SearchableApiAdapter(items) { selectedItem: School ->
            onItemSelected(selectedItem)
            bottomSheet.dismiss()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        bottomSheet.setContentView(view)
        bottomSheet.show()
    }

    private fun showSearchableBottomSheetStudentApi(
        title: String,
        items: ArrayList<StudentReg>,
        onItemSelected: (StudentReg) -> Unit
    ) {
        val bottomSheet: BottomSheetDialog = BottomSheetDialog(this)
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_searchable, null)

        val searchField: TextInputEditText = view.findViewById(R.id.searchField)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        val adapter = StudentFeeApiAdapter(items) { selectedItem: StudentReg ->
            onItemSelected(selectedItem)
            bottomSheet.dismiss()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        bottomSheet.setContentView(view)
        bottomSheet.show()
    }

    private fun validateForm(
        button: MaterialButton,
        institute: TextView,
        branch: TextView,
        regNumber: TextInputEditText
    ) {
        button.isEnabled = institute.text.toString() != getString(R.string.institute_name) &&
                branch.text.toString() != getString(R.string.branch_name) &&
                regNumber.text?.length == 14
    }

    private fun showSelectionDialog(
        title: String,
        items: List<String>,
        onSelected: (String) -> Unit
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(items.toTypedArray()) { _, which ->
                onSelected(items[which])
            }
            .show()
    }


    data class Child(val displayName: String) {
        fun getRegNumber(): String = displayName.substringAfterLast("-").trim()
    }

    private fun showChildSelection() {
        if (binding.tvBranch.text != getString(R.string.branch_name)) {
            val selectedInstitute =
                institutesApi!!.first { it.name == binding.tvInstitute.text.toString() }
            val selectedBranch =
                avialableBranches!!.first { it == binding.tvBranch.text.toString() }

            if (!avialableStudents.isNullOrEmpty()) {
                /*    showSearchableBottomSheet(
                        title = "Select Registration",
                        items = avialableStudents!!.map { it.studentName }
                    ) { selected ->
                        binding.tvRegNumber.text = selected
                        binding.tvRegNumber.setTextColor(ContextCompat.getColor(this, R.color.black))
                        binding.regNumberContainer.visibility = View.VISIBLE
                        validateForm()
                        getStudentProfileRecord(schoolId,avialableStudents.id)

                    }*/
                showSearchableBottomSheetStudentApi(
                    title = getString(R.string.registration_name),
                    items = avialableStudents as ArrayList<StudentReg>
                ) { selected ->
                    binding.tvRegNumber.text = selected.studentName
                    binding.tvRegNumber.setTextColor(ContextCompat.getColor(this, R.color.black))
                    binding.regNumberContainer.visibility = View.VISIBLE
                    validateForm()
                    STUDENT_ID = selected.id
                    myTinyDB!!.putPayStudentData(Constants.PAY_STUDENT_DATA, selected)
                    // getStudentProfileRecord(schoolId,selected.id)

                }
            } else {
                Dialogs.dialogWithCloseButton(
                    activity = this,
                    title = getString(R.string.message),
                    msg = getString(R.string.no_result_found_student),
                    btntext = getString(R.string.ok),
                    cancelable = true
                )
            }

        }
    }

    private fun resetDownstreamFields() {
        binding.tvBranch.text = getString(R.string.branch_name)
        binding.tvBranch.setTextColor(ContextCompat.getColor(this, R.color.gray))
        binding.tvBatch.text = getString(R.string.batch_name)
        binding.tvBatch.setTextColor(ContextCompat.getColor(this, R.color.gray))
        binding.tvRegNumber.text = getString(R.string.registration_name)
        binding.tvRegNumber.setTextColor(ContextCompat.getColor(this, R.color.gray))
        binding.branchContainer.visibility = View.GONE
        binding.batchContainer.visibility = View.GONE
        binding.regNumberContainer.visibility = View.GONE
        binding.btnSubmit.isEnabled = false
    }

    private fun validateForm() {
        binding.btnSubmit.isEnabled =
            binding.tvInstitute.text != getString(R.string.institute_name) &&
                    binding.tvBranch.text != getString(R.string.branch_name) &&
                    binding.tvBatch.text != getString(R.string.batch_name) &&
                    binding.tvRegNumber.text != getString(R.string.registration_name)
    }

    private fun submitForm() {
        val intent = Intent(this, FeePaymentActivity::class.java).apply {
            putExtra("INSTITUTE", binding.tvInstitute.text.toString())
            putExtra("BRANCH", binding.tvBranch.text.toString())
            putExtra("BATCH", binding.tvBatch.text.toString())
            putExtra("REG_NUMBER", binding.tvRegNumber.text.toString())
            putExtra("STUDENT_ID", STUDENT_ID)
            putExtra("PARENT_ID", parentData?.id)
            putExtra("SCHOOL_ID", schoolId)
            // Remove these field after comple response
            //   putExtra("PARENT_ID", 3)
            //  putExtra("SCHOOL_ID", 57)


        }
        startActivity(intent)
    }

    private fun getAlSchoolDetail() {
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

        viewModel.getAlSchoolDetail().observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                if (!it.school.isNullOrEmpty()) {
                    institutesApi = it.school
                }

            }

        })
    }

    private fun getSchoolDetails(schoolId: Int, parentId: Int) {
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
        if (avialableBranches != null)
            avialableBranches!!.clear()
        if (avialableSessions != null)
            avialableSessions!!.clear()
        if (avialableStudents != null)
            avialableStudents!!.clear()
        viewModel.getSchoolDetails(schoolId, parentId).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                if (!it.student!!.branches.isNullOrEmpty()) {
                    avialableBranches = it.student.branches
                    //   getAllSessions(schoolId)
                    //  getStudentsBySchool(schoolId)
                }

                if (!it.student!!.session.isNullOrEmpty()) {
                    avialableSessions = it.student!!.session
                }

                if (!it.student!!.children.isNullOrEmpty()) {
                    avialableStudents = it.student!!.children
                }

            }

        })
    }


    /*   private fun getAllBranches(schoolId: Int) {
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

           viewModel.getAllBranches(schoolId).observe(this, Observer {
               dismissProgressDialog()
               if (it.message.equals(Constants.SUCCESS)) {

                   if (!it.branch.isNullOrEmpty())
                   {
                       avialableBranches= it.branch
                       getAllSessions(schoolId)
                       getStudentsBySchool(schoolId)
                   }

               }

           })
       }

       private fun getAllSessions(schoolId: Int) {
           if (!EduPay.hasNetwork()) {
               Dialogs.dialogWithCloseButton(
                   activity = this,
                   title = getString(R.string.network_title),
                   msg = getString(R.string.network_error),
                   btntext = getString(R.string.ok),
                   cancelable = true
               )

           }
          // showProgressDialog()

           viewModel.getAllSessions(schoolId).observe(this, Observer {
               //dismissProgressDialog()
               if (it.message.equals(Constants.SUCCESS)) {

                   if (!it.session.isNullOrEmpty())
                   {
                       avialableSessions= it.session
                   }

               }

           })
       }

       private fun getStudentsBySchool(schoolId: Int) {
           if (!EduPay.hasNetwork()) {
               Dialogs.dialogWithCloseButton(
                   activity = this,
                   title = getString(R.string.network_title),
                   msg = getString(R.string.network_error),
                   btntext = getString(R.string.ok),
                   cancelable = true
               )

           }
          // showProgressDialog()
           val parentId=parentData!!.id
           val schoolId=schoolId
         //  val parentId=3
        //   val schoolId=57
           val request= ParentIdRequest(parentId)
           viewModel.getStudentsBySchool(schoolId,request).observe(this, Observer {
               //dismissProgressDialog()
               if (it.message.equals(Constants.SUCCESS)) {

                   if (!it.student.isNullOrEmpty())
                   {
                       avialableStudents= it.student
                   }

               }

           })
       }*/


}