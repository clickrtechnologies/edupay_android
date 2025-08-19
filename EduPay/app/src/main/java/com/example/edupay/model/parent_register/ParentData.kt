package com.example.edupay.model.parent_register

data class ParentData(
    val countryCode: String,
    val email: String,
    val id: Int,
    val mobileNumber: String,
    val otp: String,
    val parentId: Int,
    val photoPath: String,
    val schoolId: Int,
    val studentIdNumber: String,
    val studentName: String,
    val verified: String,
    val registrations: String,
    val representativeEmail: String,
    val representativeMobile: String,
    val representativeName: String,
)