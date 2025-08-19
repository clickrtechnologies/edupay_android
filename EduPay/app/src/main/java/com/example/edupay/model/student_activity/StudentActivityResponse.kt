package com.example.edupay.model.student_activity

import com.example.edupay.model.school_register.Note

data class StudentActivityResponse(
    val `data`: List<ActivityData>?,
    val message: String,
):Note()