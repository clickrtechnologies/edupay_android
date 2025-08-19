package com.example.edupay.school

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
import androidx.lifecycle.Observer
import com.app.conatctsync.app.EduPay
import com.example.edupay.ui.BaseActivity
import com.app.conatctsync.ui.home.HomeViewModel
import com.bumptech.glide.Glide
import com.example.edupay.R
import com.example.edupay.databinding.ActivitySchoolProfileBinding
import com.example.edupay.model.school_register.School
import com.example.edupay.model.school_register.UpdateSchoolRequest
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.util.Locale

@AndroidEntryPoint
class SchoolProfileActivity : BaseActivity() {
    private lateinit var binding: ActivitySchoolProfileBinding
    private var selectedLogoUri: Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val IMAGE_PICK_CODE = 1001
    private var SCREEN_TYPE = Constants.SCREEN_TYPE_SIGN_UP
    private var MOBILE = ""
    private var EMAIL = ""
    private var LATITUDE = ""
    private var LONGITUDE = ""
    private val viewModel: HomeViewModel by viewModels()
    var myTinyDB: MyTinyDB? = null
    var preferenceHelper: PreferenceHelper? = null

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val uri = it?.data?.data
            if (uri != null) {
                selectedLogoUri = uri
                binding.ivLogoPreview.setImageURI(uri)

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchoolProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Upload logo
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        if (intent != null) {
            if (intent.hasExtra(Constants.SCREEN_TYPE)) {
                SCREEN_TYPE = intent.getStringExtra(Constants.SCREEN_TYPE)!!
            }
            if (intent.hasExtra(Constants.MOBILE)) {
                MOBILE = intent.getStringExtra(Constants.MOBILE)!!
            }
            if (intent.hasExtra(Constants.EMAIL)) {
                EMAIL = intent.getStringExtra(Constants.EMAIL)!!
            }


        }

        if (!MOBILE.isNullOrEmpty()) {
            binding.etRepMobile.setText(MOBILE)
            binding.etRepMobile.isEnabled = false
        }
        if (!EMAIL.isNullOrEmpty()) {
            binding.etEmail.setText(EMAIL)
            binding.etEmail.isEnabled = false

        }
        binding.btnUploadLogo.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(1024) // max size in KB
                .maxResultSize(512, 512)
                .createIntent { intent ->
                    imagePickerLauncher.launch(intent)
                }
        }

        binding.ivLogoPreview.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(1024) // max size in KB
                .maxResultSize(512, 512)
                .createIntent { intent ->
                    imagePickerLauncher.launch(intent)
                }
        }

        binding.btnPickLocation.setOnClickListener {
            fetchCurrentLocation()
        }

        binding.btnSubmitProfile.setOnClickListener {
            if (validateInputs()) {

                val etSchoolName = binding.etSchoolName.text.toString().trim()
                val etSchoolAddress = binding.etSchoolAddress.text.toString().trim()
                val etRepresentativeName = binding.etRepresentativeName.text.toString().trim()
                val etEmail = binding.etEmail.text.toString().trim()
                val etRepMobile = binding.etRepMobile.text.toString().trim()
                val etMobileMoneyAcc = binding.etMobileMoneyAcc.text.toString().trim()
                val updateSchoolRequest = UpdateSchoolRequest(
                    address = etSchoolAddress,
                    googleMapLocation = "",
                    logoPath = "",
                    moneyAccount = etMobileMoneyAcc,
                    name = etSchoolName,
                    representativeMobile = etRepMobile,
                    representativeName = etRepresentativeName,
                    latitude = LATITUDE,
                    longitude = LONGITUDE
                )

                if (selectedLogoUri != null) {
                    updateSchoolLogo(selectedLogoUri!!.toFile(), updateSchoolRequest)
                } else {
                    val school = myTinyDB?.getSchoolData(Constants.SCHOOL_DATA)

                    if (school!!.logoPath.isNotEmpty()) {
                        updateSchoolRequest.logoPath=school!!.logoPath
                    }
                    schoolUpdate(updateSchoolRequest)
                }

            }
        }

        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        binding.ivLogoPreview.startAnimation(animation)

        if (!SCREEN_TYPE.equals(Constants.SCREEN_TYPE_SIGN_UP)) {
            val school = myTinyDB?.getSchoolData(Constants.SCHOOL_DATA)
            school?.let {
                populateSchoolProfileUI(it)
            }
        }
    }

    private fun fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = "Lat: ${location.latitude}, Lng: ${location.longitude}"
                    LATITUDE = location.latitude.toString()
                    LONGITUDE = location.longitude.toString()
                    Toast.makeText(this, "Location: $latLng", Toast.LENGTH_LONG).show()
                   val locationName= getAddressFromLatLng(this,location.latitude,location.longitude)
                    binding.btnPickLocation.text = locationName
                } else {
                    Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchCurrentLocation()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        val name = binding.etSchoolName.text.toString().trim()
        val address = binding.etSchoolAddress.text.toString().trim()
        val repName = binding.etRepresentativeName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val mobile = binding.etRepMobile.text.toString().trim()
        val accNo = binding.etMobileMoneyAcc.text.toString().trim()

        when {
            name.isEmpty() -> {
                showValidationDialog(getString(R.string.error_school_name_required))
                return false
            }

            address.isEmpty() -> {
                showValidationDialog(getString(R.string.error_school_address_required))
                return false
            }

            repName.isEmpty() -> {
                showValidationDialog(getString(R.string.error_rep_name_required))
                return false
            }

            email.isEmpty() -> {
                showValidationDialog(getString(R.string.error_email_required))
                return false
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showValidationDialog(getString(R.string.error_email_invalid))
                return false
            }

            mobile.isEmpty() -> {
                showValidationDialog(getString(R.string.error_mobile_required))
                return false
            }

            mobile.length < 7 -> {
                showValidationDialog(getString(R.string.error_mobile_invalid))
                return false
            }

            accNo.isEmpty() -> {
                showValidationDialog(getString(R.string.error_account_required))
                return false
            }

            else -> return true
        }
    }

    private fun showValidationDialog(message: String) {
        Dialogs.dialogWithCloseButton(
            activity = this,
            title = getString(R.string.message),
            msg = message,
            btntext = getString(R.string.ok),
            cancelable = false
        )

    }

    private fun schoolUpdate(updateSchoolRequest: UpdateSchoolRequest) {
        val schoolData = myTinyDB?.getSchoolData(Constants.SCHOOL_DATA)

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

        viewModel.updateSchool(schoolData!!.id, updateSchoolRequest).observe(this, Observer {
            dismissProgressDialog()
       /*     if (SCREEN_TYPE.equals(Constants.SCREEN_TYPE_SIGN_UP)) {
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra(Constants.LOGIN_TYPE, Constants.SIGNUP_TYPE_SCHOOL)
                startActivity(intent)
            } else {
                finish()
            }*/
            if (it.message.equals(Constants.SUCCESS)) {

                it.school?.let { school ->
                    myTinyDB!!.putSchoolData(Constants.SCHOOL_DATA, school)
                    if (SCREEN_TYPE.equals(Constants.SCREEN_TYPE_SIGN_UP)) {
                        val intent = Intent(this, PaymentActivity::class.java)
                        intent.putExtra(Constants.LOGIN_TYPE, Constants.SIGNUP_TYPE_SCHOOL)
                        startActivity(intent)
                    } else {
                        finish()
                    }
                    //  preferenceHelper!!.save(Constant.SESSION_ID, body.data.session)
                    // preferenceHelper!!.save(Constant.CHALLENGE_NAME, body.data.challengeName)
                }
            }else{
                if (SCREEN_TYPE.equals(Constants.SCREEN_TYPE_SIGN_UP)) {
                    val intent = Intent(this, PaymentActivity::class.java)
                    intent.putExtra(Constants.LOGIN_TYPE, Constants.SIGNUP_TYPE_SCHOOL)
                    startActivity(intent)
                } else {
                    finish()
                }
            }

        })
    }

    private fun updateSchoolLogo(school_logo: File, updateSchoolRequest: UpdateSchoolRequest) {

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

                it.url.let { schoollogo ->
                    preferenceHelper!!.save(Constants.SCHOOL_LOGO, schoollogo)
                    updateSchoolRequest.logoPath = schoollogo
                    schoolUpdate(updateSchoolRequest)


                }
            }

        })
    }

    fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                address.getAddressLine(0) // Full address line
            } else {
                "Address not found"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            "Unable to get address"
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            "Invalid coordinates"
        }
    }

    private fun populateSchoolProfileUI(school: School) {
        binding.etSchoolName.setText(school.name)
        binding.etSchoolAddress.setText(school.address)
        binding.etRepresentativeName.setText(school.representativeName)
        binding.etEmail.setText(school.email)
        binding.etRepMobile.setText(school.representativeMobile)
        binding.etMobileMoneyAcc.setText(school.moneyAccount)

        // Optional: Load logo image from URL/path
        if (school.logoPath.isNotEmpty()) {
            Glide.with(this)
                .load(school.logoPath)
                .placeholder(R.drawable.ic_school)
                .into(binding.ivLogoPreview)
        }

        // Optionally handle latitude and longitude
        if (school.latitude.isNotEmpty() && school.longitude.isNotEmpty()) {
            val lat = school.latitude.toDoubleOrNull()
            val lng = school.longitude.toDoubleOrNull()
            if (lat != null && lng != null) {
                val address = getAddressFromLatLng(this, lat, lng)
                binding.btnPickLocation.text = address ?: "Pick School Location (Google Map)"
            }
        }
    }

}