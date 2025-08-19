package com.example.edupay.model.announcement

import com.example.edupay.model.school_register.Note

data class ResponseSchoolAnnouncement(
    val message: String,
    val school: SchoolNotficationList?
): Note()