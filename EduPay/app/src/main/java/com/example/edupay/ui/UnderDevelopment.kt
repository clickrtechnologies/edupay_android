package com.example.edupay.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.edupay.R

class UnderDevelopment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_under_development)
        val backBtn = findViewById<Button>(R.id.btnBack)
        backBtn.setOnClickListener {
            finish() // Goes back to previous activity (e.g., main dashboard)
        }
    }
}