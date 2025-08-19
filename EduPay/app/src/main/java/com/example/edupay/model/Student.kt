package com.example.edupay.model



data class PaymentStatus(
    var isPaid: Boolean,
    var modeOfPayment: String = "N/A"
)



// Student.kt
data class Student(
    val id: Int,
    val name: String,
    val isPaid: Boolean,
    val paymentMode: String?,
    val term: Int
)


data class StudentChart(
    val id: Int,
    val name: String,
    val isPaid: Boolean,
    val paymentMode: String? = null // null for unpaid
)
