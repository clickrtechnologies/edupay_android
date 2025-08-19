package com.example.edupay.model.payment_notfication

import com.example.edupay.model.school_register.Note

data class ResponsePaymentNotifcation(
    val message: String,
    val school: PaymentNotiData?
):Note()