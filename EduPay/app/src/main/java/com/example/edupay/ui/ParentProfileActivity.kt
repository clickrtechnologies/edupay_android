package com.example.edupay.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.net.toFile
import androidx.lifecycle.Observer
import com.app.conatctsync.app.EduPay
import com.app.conatctsync.ui.home.HomeViewModel
import com.bumptech.glide.Glide
import com.example.edupay.R
import com.example.edupay.databinding.ActivityParentProfileBinding
import com.example.edupay.model.parent_register.Parent
import com.example.edupay.model.parent_register.UpdateParentDetail
import com.example.edupay.model.regstudents.StudentReg
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.school.SchoolDashboardActivity
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.ArrayList

@AndroidEntryPoint
class ParentProfileActivity : BaseActivity() {
    private var parentData: Parent? = null
    private var child_data: ArrayList<StudentReg>? = null
    private lateinit var binding: ActivityParentProfileBinding
    private var selectedLogoUri: Uri? = null
    private var FATHER_NAME: String = ""
    private var MOTHER_NAME: String = ""
    private var ADDITIONAL_EMAIL: String = ""
    private val viewModel: HomeViewModel by viewModels()
    var myTinyDB: MyTinyDB? = null
    var preferenceHelper: PreferenceHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        parentData = myTinyDB!!.getParentData(Constants.PARENT_DATA)
        child_data = myTinyDB!!.getChildData(Constants.CHILD_DATA_LIST)
        preferenceHelper = PreferenceHelper(this)
        if (intent != null) {
            if (intent.hasExtra(Constants.FATHER_NAME)) {
                FATHER_NAME = intent.getStringExtra(Constants.FATHER_NAME)!!
            }
            if (intent.hasExtra(Constants.MOTHER_NAME)) {
                MOTHER_NAME = intent.getStringExtra(Constants.MOTHER_NAME)!!
            }
            if (intent.hasExtra(Constants.ADDITIONAL_EMAIL)) {
                ADDITIONAL_EMAIL = intent.getStringExtra(Constants.ADDITIONAL_EMAIL)!!
            }

        }

        /* binding.tvParentName.text = "${getString(R.string.parent_name)}: $FATHER_NAME"
         binding.tvRegisteredMobile.text = "${getString(R.string.registered_mobile)}: ${parentData!!.countryCode}${parentData!!.mobileNumber}"
         binding.tvAdditionalMobile.text = "${getString(R.string.additional_email)}: $ADDITIONAL_EMAIL"
         binding.tvEmail.text = "${getString(R.string.email_address)}: ${parentData!!.email}"
         binding.tvMoneyAccount.text = "${getString(R.string.mobile_money_account)}: +123456789"
         binding.tvSchoolName.text = "${getString(R.string.school_name)}: Green Valley School"
         binding.tvStudentDetails.text = "Student Details: Student 1: A1234\nStudent 2: A5678"*/
     /*   if (ADDITIONAL_EMAIL != null) {
            ADDITIONAL_EMAIL = ADDITIONAL_EMAIL
        }*/
      /*  if (parentData!!.motherName != null) {
            MOTHER_NAME = parentData!!.motherName
        }*/
        if (parentData!!.fatherName != null) {
            FATHER_NAME = parentData!!.fatherName
        }

        binding.tvParentName.text = "${getString(R.string.parent_name)}: ${parentData!!.fatherName}"
        binding.tvRegisteredMobile.text =
            "${getString(R.string.registered_mobile)}: ${parentData!!.countryCode}${parentData!!.mobileNumber}"
        binding.tvAdditionalMobile.text = "${getString(R.string.additional_email)}: ${ADDITIONAL_EMAIL}"
        binding.tvEmail.text = "${getString(R.string.email_address)}: ${parentData!!.email}"

        if (!parentData!!.photoPath.isNullOrBlank()) {
            Glide.with(binding.root.context)
                .load(parentData!!.photoPath)
                .into(binding.ivSchoolLogo)
        } else {
            // binding.ivSchoolLogo.setImageResource(R.drawable.default_user) // fallback image
        }

// Mobile Money Account check
        binding.tvMoneyAccount.text = if (parentData!!.mobileMoneyAccount.isNullOrBlank()) {
            "${getString(R.string.mobile_money_account)}: No account associated"
        } else {
            "${getString(R.string.mobile_money_account)}: ${parentData!!.mobileMoneyAccount}"
        }

// School Name – assuming static or from API


        binding.tvSchoolName.text = "${getString(R.string.school_name)}: ${"No Data available"}"

// Build child details dynamically
        val childDetails = StringBuilder()
        child_data!!.forEachIndexed { index, child ->
            childDetails.append("Student ${index + 1}: ${child.studentName} (${child.studentIdNumber}) - Class ${child.className} ${child.section}\n")
        }
        binding.tvStudentDetails.text = childDetails.toString().trim()


        binding.btnUploadPhoto.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(1024) // max size in KB
                .maxResultSize(512, 512)
                .createIntent { intent ->
                    imagePickerLauncher.launch(intent)
                }
        }

        binding.btnGoToDashboard.setOnClickListener {

            if (selectedLogoUri != null) {
                updateParentLogo(selectedLogoUri!!.toFile())
            } else {
                if (!parentData!!.photoPath.isNullOrEmpty()) {
                    preferenceHelper!!.save(Constants.PARENT_LOGO, parentData!!.photoPath)
                    updateParentInfo(parentData!!.photoPath)
                } else {
                    Constants.showValidationDialog(this, getString(R.string.upload_image))

                }
            }

            /*   if (selectedLogoUri == null) {
                   Toast.makeText(this, getString(R.string.upload_image), Toast.LENGTH_SHORT).show()
               } else {
                   updateParentLogo(selectedLogoUri!!.toFile())

               }*/
        }
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val uri = it?.data?.data
            if (uri != null) {
                selectedLogoUri = uri
                binding.ivSchoolLogo.setImageURI(uri)
            }
        }


    private fun updateParentLogo(school_logo: File) {

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
        viewModel.updateSchoolLogo(school_logo).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                it.url.let { parentlogo ->
                    preferenceHelper!!.save(Constants.PARENT_LOGO, parentlogo)
                    updateParentInfo(parentlogo)

                }
            }

        })
    }


    private fun updateParentInfo(parentlogo: String) {

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
        val request =
            UpdateParentDetail(
                additionalEmail = ADDITIONAL_EMAIL,
                fatherName = FATHER_NAME,
                motherName = MOTHER_NAME,
                photoPath = parentlogo
            )
        viewModel.updateParentInfo(parentData!!.id, request).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                it.parent?.let { parent ->
                    //  myTinyDB!!.putParentData(Constants.PARENT_DATA, parent)
                    preferenceHelper!!.save(Constants.SCREEN_TYPE, Constants.DASHBOARD)
                    val intent = Intent(this, SchoolDashboardActivity::class.java)
                    intent.putExtra(Constants.LOGIN_TYPE, Constants.LOGIN_TYPE_PARENT)
                    startActivity(intent)
                    finishAffinity()
                }
            } else {
                if (!it.note.isNullOrEmpty()) {
                    Constants.showValidationDialog(this, it.note)
                }
            }

        })
    }

}