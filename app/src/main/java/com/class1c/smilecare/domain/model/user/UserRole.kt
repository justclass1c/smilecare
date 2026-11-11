package com.class1c.smilecare.domain.model.user

/**
 * The closed set of account types in SmileCare.
 *
 * A closed enum keeps role checks exhaustive and prevents typo-prone string roles.
 */
enum class UserRole {
    PATIENT,
    DENTIST,
    RECEPTIONIST,
    ADMIN
}
