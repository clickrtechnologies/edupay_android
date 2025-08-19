package com.example.edupay.model.student_fees_data

data class SelectableFeesProfile(
    val profile: FeesProfile,
    var isSelected: Boolean = false,
    var includeUniform: Boolean = false,
    var includeStationary: Boolean = false,
    var includeTransport: Boolean = false
)
