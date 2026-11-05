package com.class1c.smilecare.features.appointments.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.class1c.smilecare.domain.usecase.CancelAppointmentUseCase
import com.class1c.smilecare.domain.usecase.GetAppointmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentsViewModel @Inject constructor(
    private val getAppointments: GetAppointmentsUseCase,
    private val cancelAppointment: CancelAppointmentUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppointmentsUiState>(AppointmentsUiState.Loading)
    val uiState: StateFlow<AppointmentsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAppointments()
                .catch { throwable ->
                    _uiState.value = AppointmentsUiState.Error(
                        throwable.message ?: "Could not load appointments",
                    )
                }
                .collect { appointments ->
                    _uiState.value = if (appointments.isEmpty()) {
                        AppointmentsUiState.Empty
                    } else {
                        AppointmentsUiState.Success(appointments)
                    }
                }
        }
    }

    fun onEvent(event: AppointmentsUiEvent) {
        when (event) {
            is AppointmentsUiEvent.CancelAppointment -> viewModelScope.launch {
                cancelAppointment(event.appointmentId)
            }
        }
    }
}
