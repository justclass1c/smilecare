package com.class1c.smilecare.domain.model.user

/**
 * Shared identity contract for every account in SmileCare.
 *
 * This is a sealed interface, not an `open class`, for two reasons:
 * - `data class` cannot inherit from another `data class`, so the subtypes stay data classes
 *   with working `copy()`/`equals()`/destructuring.
 * - A sealed hierarchy makes `when (user)` exhaustive: adding a role breaks compilation
 *   everywhere it must be handled.
 *
 * Security note: [password] is kept here because it is part of the assignment's model.
 * In a production app the domain should hold a `passwordHash` (or no password at all),
 * and the raw credential should only ever travel through the auth layer.
 */
sealed interface User {
    val id: String
    val name: String
    val email: String
    val password: String
    val nric: String
    val phoneNo: String
    val age: Int
    val gender: Char
    val role: UserRole
}
