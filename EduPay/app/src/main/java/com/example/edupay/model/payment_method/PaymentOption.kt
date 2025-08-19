package com.example.edupay.model.payment_method

data class PaymentOption(
    val createdAt: String,
    val id: Int,
    val paymenOption: String,
    val schoolId: Int,
    val status: String,
    val updatedAt: String
)