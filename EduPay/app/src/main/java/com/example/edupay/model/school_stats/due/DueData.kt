package com.example.edupay.model.school_stats.due

import com.example.edupay.model.school_stats.Student

data class DueData(
    val statics: DueStatics,
    val students: ArrayList<Student>
)