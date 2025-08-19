package com.example.edupay.model.parent_register

data class UpdateParentInfoRequest(
    val additionalOptions: String,
    val address: String,
    val batch: String,
    val branch: String,
    val googleMapLocation: String,
    val grade: String,
    val id: Int,
    val instituteName: String,
    val logoPath: String,
    val mobileMoneyAccount: String,
    val registrations: String,
    val representativeEmail: String,
    val representativeMobile: String,
    val representativeName: String,
    val schoolId: Int,
    val term: String,
    val termFees: String,
    val totalFees: String,
    val transactionId: String
)