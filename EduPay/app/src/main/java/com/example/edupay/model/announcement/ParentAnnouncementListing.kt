package com.example.edupay.model.announcement

import com.example.edupay.model.school_register.Note

data class ParentAnnouncementListing(
    val message: String,
    val data: ArrayList<SchoolNotficationList>?
): Note()