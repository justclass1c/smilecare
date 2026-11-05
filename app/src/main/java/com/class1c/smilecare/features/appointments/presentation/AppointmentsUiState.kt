package com.class1c.smilecare.features.appointments.presentation

import com.class1c.smilecare.domain.model.Appointment

sealed interface AppointmentsUiState {
    data object Loading : AppointmentsUiState
    data object Empty : AppointmentsUiState
    data class Success(val appointments: List<Appointment>) : AppointmentsUiState
    data class Error(val message: String) : AppointmentsUiState
}
