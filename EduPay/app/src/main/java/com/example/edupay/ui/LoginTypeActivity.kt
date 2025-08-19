package com.example.edupay.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.edupay.databinding.ActivityLoginTypeBinding
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.school.SchoolLoginActivity
import com.example.edupay.school.SchoolRegistrationActivity
import com.example.edupay.utils.Constants
import com.google.firebase.messaging.FirebaseMessaging

class LoginTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginTypeBinding
    var preferenceHelper: PreferenceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_login_type)

        binding = ActivityLoginTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceHelper = PreferenceHelper(this)

        binding.cardParentLogin.setOnClickListener {
            onParentLoginClick()
        }
        binding.cardSchoolLogin.setOnClickListener {
            onSchoolLoginClick()
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM_TOKEN", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("FCM_TOKEN", "Current token: $token")
            // Example: Save the token
            preferenceHelper!!.save(Constants.FIREBASE_TOKEN, token)

            // Send token to your server if needed
            // sendTokenToServer(token)
        }

    }

    fun onParentLoginClick() {
        val intent = Intent(this, ParentLoginActivity::class.java)
        intent.putExtra(Constants.LOGIN_TYPE, Constants.LOGIN_TYPE_PARENT)
        startActivity(intent)
    }

    fun onSchoolLoginClick() {
        val intent = Intent(this, SchoolLoginActivity::class.java)
        intent.putExtra(Constants.LOGIN_TYPE, Constants.LOGIN_TYPE_SCHOOL)
         startActivity(intent)
    }

}