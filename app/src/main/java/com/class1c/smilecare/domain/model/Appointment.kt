package com.class1c.smilecare.domain.model

/**
 * Core business model. Pure Kotlin: no Android, no network/database types.
 * This is the single source of truth for what an appointment *is*.
 */
data class Appointment(
    val appointmentID: String,
    val patientID: String, // foreign key, use reference
    val dentistID: String, // foreign key, use reference
    val date: String,
    val startTime: String,
    val endTime: String?, // nullable. endTime to be filled in after appointment
    val reason: String,
    val status: AppointmentStatus,
)

enum class AppointmentStatus {
    SCHEDULED,
    COMPLETED,
    CANCELLED,
}
