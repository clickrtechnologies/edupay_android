package com.example.edupay.model.payment_notfication

import com.example.edupay.model.school_register.Note


data class ResponseSchoolNotification(
    val message: String,
    val school: List<PaymentNotiData>?
): Note()