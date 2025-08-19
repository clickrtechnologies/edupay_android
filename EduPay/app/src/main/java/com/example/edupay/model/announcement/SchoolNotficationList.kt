package com.example.edupay.model.announcement

data class SchoolNotficationList(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val status: String,
    val schoolId: String,
    val school: Any,

)