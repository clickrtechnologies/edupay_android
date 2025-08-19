package com.example.edupay.model.parent_payment

data class PaymentListingData(
    val amountPaid: Double,
    val currency: String,
    val endDate: String,
    val id: Int,
    val parentId: Int,
    val paymentDate: String,
    val paymentMethod: String,
    val schoolId: Int,
    val schoolName: String,
    val serviceCharges: Double,
    val startDate: String,
    val stationaryFee: Double,
    val status: String,
    val studentId: Int,
    val studentName: String,
    val term1Fee: String,
    val term2Fee: String,
    val term3Fee: String,
    val transectionId: String,
    val transportationFee: Double,
    val tuitionFee: Double,
    val uniformFee: Double
)