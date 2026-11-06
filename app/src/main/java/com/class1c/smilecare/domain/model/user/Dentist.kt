package com.class1c.smilecare.domain.model.user

/**
 * A dentist account: every [User] field plus the clinical specialty.
 *
 * Note: this is the *account* model. The existing
 * [com.class1c.smilecare.domain.model.Dentist] is the public directory/profile
 * model (rating, years of experience, favorite). They can be merged later once
 * the dentists feature is migrated to accounts.
 */
data class Dentist(
    override val id: String,
    override val name: String,
    override val email: String,
    override val password: String,
    override val nric: String,
    override val phoneNo: String,
    override val age: Int,
    override val gender: Char,
    val specialty: String,
) : User {

    override val role: UserRole = UserRole.DENTIST
}
