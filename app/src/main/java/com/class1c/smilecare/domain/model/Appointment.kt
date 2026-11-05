package com.class1c.smilecare.domain.model

/**
 * Core business model. Pure Kotlin: no Android, no network/database types.
 * This is the single source of truth for what an appointment *is*.
 */
data class Appointment(
    val id: String,
    val dentistName: String,
    val date: String,
    val time: String,
    val status: AppointmentStatus,
)

enum class AppointmentStatus {
    UPCOMING,
    COMPLETED,
    CANCELLED,
}
