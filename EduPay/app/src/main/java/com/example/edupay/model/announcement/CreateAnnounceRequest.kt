package com.example.edupay.model.announcement

data class CreateAnnounceRequest(
    val date: String,
    val description: String,
    val status: String,
    val title: String,
    val schoolId: Int,
)