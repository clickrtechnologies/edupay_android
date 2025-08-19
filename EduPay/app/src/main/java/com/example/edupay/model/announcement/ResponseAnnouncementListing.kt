package com.example.edupay.model.announcement

import com.example.edupay.model.school_register.Note

data class ResponseAnnouncementListing(
    val message: String,
    val school: ArrayList<SchoolNotficationList>?
): Note()