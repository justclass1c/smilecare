package com.class1c.smilecare.features.auth.register.presentation

data class RegisterUIState(
    // input fields
    val fullName: String = "",
    val dateOfBirth: String = "",
    val genderIndex: Int = 0,
    val nric: String = "",
    val email: String = "",
    val phoneNo: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    // patient-exclusive fields
    val allergies: String = "",
    val hasInsurance: Boolean = false,

    // dentist-exclusive fields
    val specialization: String = "",

    // receptionist-exclusive fields
    val employeeID: String = "",
)