package com.example.edupay.model.payment

data class PaymentParentRequest(
    val feesPaid: Boolean,
    val parentId: Int,
    val registrationNumber: String,
    val schoolUniformFees: Double,
    val studentId: Int,
    val studentStationaryFees: Double,
    val transportionFees: Double,
    val term: String,
    val transportionFeesPaid:Boolean,
    val studentStationaryFeesPaid:Boolean,
    val schoolUniformFeesPaid:Boolean
)



