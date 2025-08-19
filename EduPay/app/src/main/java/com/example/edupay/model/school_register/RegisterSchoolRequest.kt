package com.example.edupay.model.school_register

data class RegisterSchoolRequest(
    val email: String,
    val mobileNumber: String,
    val countryCode: String,
    var deviceToken: String
)