package com.example.edupay.model.student_fees_data

data class FeesProfile(
    val feesPaid: Boolean,
    val parentId: Int,
    val registrationNumber: String,
    val schoolId: Int,
    val schoolUniformFees: Double,
    val studentId: Int,
    val studentStationaryFees: Double,
    val term: String,
    val termFees: Double,
    val totalFees: Double,
    val transactionId: String,
    val transportionFees: Double
)