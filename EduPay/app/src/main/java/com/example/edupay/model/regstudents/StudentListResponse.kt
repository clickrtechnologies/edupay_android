package com.example.edupay.model.regstudents

data class StudentListResponse(
    val message: String,
    val student: List<StudentReg>?
)