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
    override val age: Int,
    override val gender: Char,
    val address: String,
    val emergencyContact: String,
) : User {

    override val role: UserRole = UserRole.PATIENT
}
