package com.class1c.smilecare.features.home.presentation

import com.class1c.smilecare.domain.model.Appointment

/** Everything the Home screen can render. Immutable, produced only by the ViewModel. */
sealed interface HomeUiState {
    data object Loading : HomeUiState
    data object Empty : HomeUiState
    data class Success(val appointments: List<Appointment>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
