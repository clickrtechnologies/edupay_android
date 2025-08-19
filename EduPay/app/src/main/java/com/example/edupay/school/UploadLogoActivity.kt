package com.example.edupay.school

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.edupay.R
import com.example.edupay.databinding.ActivityPaymentStatsBinding
import com.example.edupay.databinding.ActivityUploadLogoBinding

class UploadLogoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadLogoBinding
    private val IMAGE_PICK_CODE = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadLogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUploadLogo.setOnClickListener {
            openGallery()
        }

        binding.btnSaveLogo.setOnClickListener {
            Toast.makeText(this, "Save logic to be implemented later", Toast.LENGTH_SHORT).show()
        }


    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            binding.ivLogoPreview.setImageURI(imageUri)
        }
    }
}