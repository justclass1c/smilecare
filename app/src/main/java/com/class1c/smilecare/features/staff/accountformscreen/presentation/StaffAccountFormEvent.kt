package com.class1c.smilecare.features.staff.accountformscreen.presentation

import com.class1c.smilecare.features.auth.register.presentation.RegisterEvent
import com.class1c.smilecare.features.auth.register.presentation.RegisterField

enum class StaffAccountFormField {
    // users attribute
    NAME, DATEOFBIRTH, GENDER, NRIC, EMAIL, PHONENO, PASSWORD, CONFIRMPASSWORD,
    
    // dentist-exclusive
    SPECIALTY
}
sealed interface StaffAccountFormEvent {
    // users event
    data class UpdateFullName(val fullName: String): StaffAccountFormEvent
    data class UpdateDateOfBirth(val dateOfBirth: String): StaffAccountFormEvent
    data class UpdateGender(val genderIndex: Int): StaffAccountFormEvent
    data class UpdateNRIC(val nric: String): StaffAccountFormEvent
    data class UpdateEmail(val email: String): StaffAccountFormEvent
    data class UpdatePhoneNo(val phoneNo: String): StaffAccountFormEvent
    data class UpdatePassword(val password: String): StaffAccountFormEvent
    data class UpdateConfirmPassword(val password: String, val confirmPassword: String): StaffAccountFormEvent
    data class UpdateRoleIndex(val roleIndex: Int): StaffAccountFormEvent

    // patient exclusive
    data class UpdateAllergies(val allergies: String): StaffAccountFormEvent
    data object ToggleHasInsurance: StaffAccountFormEvent

    // dentist exclusive
    data class UpdateSpecialization(val specialization: String): StaffAccountFormEvent

    // input validation event
    data class ValidateInput(val field: StaffAccountFormField): StaffAccountFormEvent

    // create account event
    data object RegisterAccount: StaffAccountFormEvent
    data object SaveAccountChanges: StaffAccountFormEvent
}