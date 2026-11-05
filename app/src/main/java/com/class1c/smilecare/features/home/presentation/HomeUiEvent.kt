package com.class1c.smilecare.features.home.presentation

/** Everything the Home screen can ask for. The screen never calls a repository or use case directly. */
sealed interface HomeUiEvent {
    data class CancelAppointment(val appointmentId: String) : HomeUiEvent
}
