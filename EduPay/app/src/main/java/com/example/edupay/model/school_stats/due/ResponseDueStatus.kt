package com.example.edupay.model.school_stats.due

data class ResponseDueStatus(
    val `data`: DueData?,
    val message: String,
    val note: String
)