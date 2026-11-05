package com.class1c.smilecare.features.appointments.presentation

sealed interface AppointmentsUiEvent {
    data class CancelAppointment(val appointmentId: String) : AppointmentsUiEvent
}
