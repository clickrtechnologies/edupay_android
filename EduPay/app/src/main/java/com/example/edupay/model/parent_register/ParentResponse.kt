package com.example.edupay.model.parent_register

import com.example.edupay.model.regstudents.StudentReg
import com.example.edupay.model.school_register.Note

data class ParentResponse(
    val access_token: String,
    val message: String,
    val parent: Parent?,
    val child: ArrayList<StudentReg>?,
    val paymentMehod: ArrayList<PaymentMehod>?
):Note()