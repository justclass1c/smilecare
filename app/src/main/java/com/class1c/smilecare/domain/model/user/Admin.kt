package com.class1c.smilecare.domain.model.user

data class Admin(
    override val id: String,
    override val name: String,
    override val email: String,
    override val password: String,
    override val nric: String,
    override val phoneNo: String,
    override val dateOfBirth: String,
    override val gender: Char,
    override val role: UserRole = UserRole.ADMIN
): User