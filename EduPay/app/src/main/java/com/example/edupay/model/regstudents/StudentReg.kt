package com.example.edupay.model.regstudents

data class StudentReg(
    val id: Int,
    val schoolId: Int,
    val parentId: Int,
    val registrationNumber: String,
    val studentName: String,
    val batch: String,
    val studentIdNumber: String,
    val className: String,
    val section: String,
    val address: String,
    val photoPath: String,
    val gender: String,
    val dob: String,
    val verified: Boolean,
    val createdAt: String,
    val updatedAt: String,
)

