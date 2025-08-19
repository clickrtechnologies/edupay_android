package com.example.edupay.model.student_activity

data class ActivityData(
    val activityDescription: String,
    val activityName: String,
    val currentDate: String,
    val id: Int,
    val schoolId: Int,
    val totalNoOfParticipants: String,
    val updatedDate: String,
    val venue: String,
    val winners: String
)