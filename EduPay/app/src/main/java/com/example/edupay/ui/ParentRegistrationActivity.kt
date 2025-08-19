package com.example.edupay.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.databinding.ActivityParentRegistrationBinding
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParentRegistrationActivity : BaseActivity() {
    private lateinit var binding: ActivityParentRegistrationBinding
    private val viewModel: HomeViewModel by viewModels()
    var myTinyDB: MyTinyDB? = null
    var preferenceHelper: PreferenceHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        binding.ivLogo.startAnimation(animation)

        val email = intent.getStringExtra("email") ?: ""
        val mobile = intent.getStringExtra("mobile") ?: ""
        binding.etEmail.setText(email)
        binding.etMobile.setText(mobile)

       val parentData= myTinyDB!!.getParentData(Constants.PARENT_DATA)
        if(parentData!=null)
        {
            binding.etFatherName.setText(parentData.fatherName)
            binding.etMotherName.setText(parentData.motherName)
            binding.etEmail.setText(parentData.email)
            binding.etAltEmail.setText(parentData.additionalEmail)
            binding.etMobile.setText("${parentData.countryCode} ${parentData.mobileNumber}")
        }

        // Attach CCP to mobile number field

        binding.btnSubmit.setOnClickListener {
            val fatherName = binding.etFatherName.text.toString().trim()
            val motherName = binding.etMotherName.text.toString().trim()
            val regEmail = binding.etEmail.text.toString().trim()
            val altEmail = binding.etAltEmail.text.toString().trim()

            if (fatherName.isEmpty()) {
                binding.etFatherName.error = "Required"
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(regEmail).matches()) {
                binding.etEmail.error = "Invalid registered email"
                return@setOnClickListener
            }

            // TODO: Check if email is valid/registered (API or DB check)
            preferenceHelper!!.save(Constants.SCREEN_TYPE,Constants.REG)
          //  Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ParentProfileActivity::class.java)
            intent.putExtra(Constants.FATHER_NAME,fatherName)
            intent.putExtra(Constants.MOTHER_NAME,motherName)
            intent.putExtra(Constants.ADDITIONAL_EMAIL,altEmail)
            startActivity(intent)
            finish()
        }

    }

}