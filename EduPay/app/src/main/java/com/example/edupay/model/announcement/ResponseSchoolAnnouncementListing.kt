package com.example.edupay.model.announcement


data class ResponseSchoolAnnouncementListing(
    val message: String,
    val school: List<SchoolNotficationList>
)