package com.example.edupay.model.parent_register

data class PaymentMehod(
    val accountHolderName: String,
    val accountNumber: String,
    val bankAccountNumber: String,
    val bankName: String,
    val cardExpiryDate: String,
    val ccv: String,
    val crdbReferenceNumber: String,
    val createdDate: String,
    val id: Int,
    val mobileMoneyAccountNumber: String,
    val parentId: Int,
    val paymentMode: String,
    val pinNumber: String,
    val updatedDate: String
)