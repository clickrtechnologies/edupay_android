package com.example.edupay.model.payment_notfication

data class PaymentNotificationRequest(
    val branch: String,
    val message: String,
    val schoolId: Int,
    val title: String
)