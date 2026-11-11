package com.class1c.smilecare.domain.model.user

/**
 * A receptionist account: every [User] field plus the staff details needed to
 * identify where the person works.
 *
 * `employeeId` is the clinic's own staff number (not the account [User.id]),
 * and `branch` says which clinic location the desk belongs to.
 */
data class Nurse(
    override val id: String,
    override val name: String,
    override val email: String,
    override val password: String,
    override val nric: String,
    override val phoneNo: String,
    override val dateOfBirth: String,
    override val gender: Char,
    val nurseId: String,
) : User {

    override val role: UserRole = UserRole.RECEPTIONIST
}
