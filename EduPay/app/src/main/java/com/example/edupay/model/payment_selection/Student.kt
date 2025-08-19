package com.example.edupay.model.payment_selection

import com.example.edupay.model.regstudents.StudentReg

data class Student(
    val branches: ArrayList<String>,
    val children: ArrayList<StudentReg>,
    val session: ArrayList<String>
)