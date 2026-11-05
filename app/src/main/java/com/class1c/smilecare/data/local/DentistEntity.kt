package com.class1c.smilecare.data.local

/**
 * Data-layer representation of a dentist.
 *
 * In a real app this would be a Room `@Entity` or a network DTO. Keeping it
 * separate from the domain model means storage changes never leak upward.
 */
data class DentistEntity(
    val id: String,
    val fullName: String,
    val specialty: String,
    val rating: Double,
    val experienceInYears: Int,
    val favorite: Boolean,
)
