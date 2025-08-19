package com.example.edupay.model.payment_notfication

data class PaymentNotiData(
    val branch: String,
    val id: Int,
    val message: String,
    val schoolId: Int,
    val title: String
)