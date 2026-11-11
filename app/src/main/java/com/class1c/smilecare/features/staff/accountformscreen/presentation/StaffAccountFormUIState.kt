package com.class1c.smilecare.features.staff.accountformscreen.presentation

import com.class1c.smilecare.domain.model.user.UserRole

data class StaffAccountFormUIState(
    // input fields
    val fullName: String = "",
    val dateOfBirth: String = "",
    val genderIndex: Int = 0,
    val nric: String = "",
    val email: String = "",
    val phoneNo: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val roleIndex: Int = 0,

    // patient-exclusive fields
    val allergies: String = "",
    val medicalNotes: String = "",
    val hasInsurance: Boolean = false,

    // dentist-exclusive fields
    val dentistId: String = "",
    val specialization: String = "",

    // receptionist-exclusive fields
    val nurseId: String = "",

    // validation states
    // nullable error message: null = field is valid, non-null = reason shown to the user
    val invalidName: String? = null,
    val invalidDateOfBirth: String? = null,
    val invalidGender: String? = null,
    val invalidNric: String? = null,
    val invalidEmail: String? = null,
    val invalidPhoneNo: String? = null,
    val invalidPassword: String? = null,
    val invalidConfirmPassword: String? = null,
    val invalidAllergies: String? = null,
    val invalidSpecialization: String? = null,
    val invalidEmployeeID: String? = null,

    val isLoading: Boolean = false,

    // set when register() fails (e.g. email already registered)
    val registerError: String? = null,

    // true when account creation succeeded (navigation trigger)
    val isRegistered: Boolean = false,
    val successMessage: String? = null
)