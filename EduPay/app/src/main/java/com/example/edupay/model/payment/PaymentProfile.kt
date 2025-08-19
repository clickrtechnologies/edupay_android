package com.example.edupay.model.payment

data class PaymentProfile(
    val address: String,
    val batch: String,
    val branch: String,
    val feesPaid: Boolean,
    val googleMapLocation: String,
    val grade: String,
    val id: Int,
    val instituteName: String,
    val parentId: Int,
    val photoPath: String,
    val registrationNumber: String,
    val schoolId: Int,
    val schoolUniformFees: Double,
    val studentId: Int,
    val studentStationaryFees: Double,
    val term: String,
    val termFees: Double,
    val totalFees: String,
    val transactionId: String,
    val transportionFees: Double
)