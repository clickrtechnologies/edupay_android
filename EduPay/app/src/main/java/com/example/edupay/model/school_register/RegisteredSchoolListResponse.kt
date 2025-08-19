package com.example.edupay.model.school_register

data class RegisteredSchoolListResponse(
    val message: String,
    val access_token:String,
    val school: ArrayList<School>?
): Note()