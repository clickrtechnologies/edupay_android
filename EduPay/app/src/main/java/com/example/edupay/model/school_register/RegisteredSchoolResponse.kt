package com.example.edupay.model.school_register

data class RegisteredSchoolResponse(
    val message: String,
    val access_token:String,
    val school: School?
): Note()