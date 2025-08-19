package com.example.edupay.model.school_register

data class School(
    val address: String,
    val email: String,
    val googleMapLocation: String,
    val id: Int,
    val logoPath: String,
    val mobileNumber: String,
    val moneyAccount: String,
    val name: String,
    val representativeMobile: String,
    val representativeName: String,
    val countryCode: String,
    val otp: String,
    val otpExpiryTime: String?,
    val latitude: String,
    val longitude: String,
    val subscribe: Boolean
)