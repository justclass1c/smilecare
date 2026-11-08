package com.class1c.smilecare.features.auth.register.presentation

// event listeners detect every change of state.
sealed interface RegisterEvent {
    // general template to write this is:
    // data class updateEvent(val input: DataType): Event
    // for buttons or toggles, use data object:
    // data object Event: Event

    // users event
    data class UpdateFullName(val fullName: String): RegisterEvent
    data class UpdateDateOfBirth(val dateOfBirth: String): RegisterEvent
    data class UpdateGender(val genderIndex: Int): RegisterEvent
    data class UpdateNRIC(val nric: String): RegisterEvent
    data class UpdateEmail(val email: String): RegisterEvent
    data class UpdatePhoneNo(val phoneNo: String): RegisterEvent
    data class UpdatePassword(val password: String): RegisterEvent
    data class UpdateConfirmPassword(val confirmPassword: String): RegisterEvent

    // patient exclusive
    data class UpdateAllergies(val allergies: String): RegisterEvent
    data object ToggleHasInsurance: RegisterEvent

    // dentist exclusive
    data class UpdateSpecialization(val specialization: String): RegisterEvent
}