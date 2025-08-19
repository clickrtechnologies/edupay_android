package com.example.edupay.model.parent_payment

import com.example.edupay.model.school_register.Note

data class PaymentListingResponse(
    val message: String,
    val payments: List<PaymentListingData>?
):Note()