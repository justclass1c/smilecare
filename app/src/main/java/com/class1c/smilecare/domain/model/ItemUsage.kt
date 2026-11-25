package com.class1c.smilecare.domain.model

/**
 * Join entity recording which items were consumed by an appointment.
 *
 * Composite primary key: (appointmentID, itemID).
 * Both fields are foreign keys: appointmentID -> Appointment, itemID -> Item.
 */
data class ItemUsage(
    val appointmentID: String, // a primary and foreign key
    val itemID: String, // a primary and foreign key
)
