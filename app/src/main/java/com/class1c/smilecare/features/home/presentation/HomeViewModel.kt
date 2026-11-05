package com.class1c.smilecare.features.home.presentation

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

/**
 * UDF loop:
 * screen -> [HomeUiEvent] -> onEvent() -> use case -> repository flow -> [HomeUiState] -> screen.
 *
 * Hilt builds this class because of @HiltViewModel + @Inject; the screen just asks
 * for it with hiltViewModel().
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAppointments: GetAppointmentsUseCase,
    private val cancelAppointment: CancelAppointmentUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAppointments()
                .catch { throwable ->
                    _uiState.value = HomeUiState.Error(
                        throwable.message ?: "Could not load appointments",
                    )
                }
                .collect { appointments ->
                    _uiState.value = if (appointments.isEmpty()) {
                        HomeUiState.Empty
                    } else {
                        HomeUiState.Success(appointments)
                    }
                }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.CancelAppointment -> viewModelScope.launch {
                cancelAppointment(event.appointmentId)
            }
        }
    }
}
