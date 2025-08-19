package com.example.edupay.model.school_register

data class UpdateSchoolRequest(
    val address: String,
    val googleMapLocation: String,
    var logoPath: String,
    val moneyAccount: String,
    val name: String,
    val representativeMobile: String,
    val representativeName: String,
    val latitude: String,
    val longitude: String,
)