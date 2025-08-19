package com.example.edupay.model.payment_method

import com.example.edupay.model.school_register.Note

data class PaymentMethodResponse(
    val message: String,
    val paymentOptions: List<PaymentOption>?
):Note()