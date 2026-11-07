package com.class1c.smilecare.domain.model.user

/**
 * A patient account.
 *
 * `role` is set by the class itself, never passed by callers, so a [Patient]
 * can never accidentally carry `UserRole.DENTIST`.
 */
data class Patient(
    override val id: String,
    override val name: String,
    override val email: String,
    override val password: String,
    override val nric: String,
    override val phoneNo: String,
    override val dateOfBirth: String,
    override val gender: Char,
    val allergies: String,
    val medicalNotes: String
) : User {

    override val role: UserRole = UserRole.PATIENT
}
