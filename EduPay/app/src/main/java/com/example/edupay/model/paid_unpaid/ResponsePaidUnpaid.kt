package com.example.edupay.model.paid_unpaid

import com.example.edupay.model.school_register.Note

data class ResponsePaidUnpaid(
    val message: String,
    val payments: ArrayList<Payment>?
): Note()