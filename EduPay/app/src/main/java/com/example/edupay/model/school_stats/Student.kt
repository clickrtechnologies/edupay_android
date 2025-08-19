package com.example.edupay.model.school_stats

data class Student(
    val id: Int,
    val payment: String,
    val paymentMode: String,
    val studentName: String,
    val fatherName: String,
    val motherName: String,
    val section: String,
    val className: String,
)