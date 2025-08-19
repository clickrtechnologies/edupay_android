package com.example.edupay.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.edupay.databinding.ActivityTransportationBinding

class TransportationActivity : AppCompatActivity() {
    private lateinit var btnTrackBus: Button
    private lateinit var tvStatus: TextView
    private lateinit var binding: ActivityTransportationBinding

    // Temporary flag – later link to real data
    private var isTransportSubscribed = false // change to true to test access

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransportationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvTransportStatus.text = if (isTransportSubscribed) {
            "You have subscribed to transport services."
        } else {
            "You have not subscribed to transport services."
        }

        binding. btnTrackBus.setOnClickListener {
            if (isTransportSubscribed) {
                Toast.makeText(
                    this,
                    "You have not subscribed to transportation services",
                    Toast.LENGTH_LONG
                ).show()
             //   startActivity(Intent(this, TransportMapActivity::class.java))
            } else {
                Toast.makeText(
                    this,
                    "You have not subscribed to transportation services",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
