package com.example.edupay.model.payment

data class PaymentResponsess(
    val message: String,
    val profile: List<PaymentProfile>?
)