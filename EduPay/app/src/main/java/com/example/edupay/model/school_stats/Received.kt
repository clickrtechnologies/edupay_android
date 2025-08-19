package com.example.edupay.model.school_stats

data class Received(
    val statics: ReceivedStatics,
    val students: ArrayList<Student>
)