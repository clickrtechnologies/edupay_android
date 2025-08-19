package com.example.edupay.model.payment.partialPayment

data class PartialPaymentRequest(
    val feesPaid: Boolean,
    val outstandingAmount: Double,
    val paidAmount: Double,
    val parentId: Int,
    val registrationNumber: String,
    val schoolId: Int,
    val schoolUniformFees: Double,
    val schoolUniformFeesPaid: Boolean,
    val studentId: Int,
    val studentStationaryFees: Double,
    val studentStationaryFeesPaid: Boolean,
    val term: String,
    val termFees: Double,
    val transportionFees: Double,
    val transportionFeesPaid: Boolean,
)