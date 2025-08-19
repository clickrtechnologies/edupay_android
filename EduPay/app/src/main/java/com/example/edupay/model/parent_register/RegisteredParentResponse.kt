package com.example.edupay.model.parent_register

import com.example.edupay.model.school_register.Note

data class RegisteredParentResponse(
    val access_token: String,
    val message: String,
    val student: ParentData?
):Note()