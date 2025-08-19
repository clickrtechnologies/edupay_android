package com.example.edupay.model.student_fees_data

data class VerifyStudentFeesResponse(
    val profile: List<FeesProfile>?,
    val message: String

)