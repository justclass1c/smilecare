package com.class1c.smilecare.domain.model

/**
 * Join entity for the many-to-many relationship between appointments and services.
 *
 * Composite primary key: (appointmentID, serviceID).
 * Both fields are foreign keys: appointmentID -> Appointment, serviceID -> Service.
 */
data class AppointmentService(
    val appointmentID: String,
    val serviceID: String,
)
