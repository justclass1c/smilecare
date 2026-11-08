package com.class1c.smilecare.features.auth.register.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(): ViewModel() {
    // private one ensures only ViewModel can modify it// private one ensures only ViewModel can modify it. holds current state
    private val _uiState = MutableStateFlow(RegisterUIState())
    // readonly StateFlow. UI can only see this and render what is in here.
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    fun onEvent(event: RegisterEvent) = when(event) {
        RegisterEvent.ToggleHasInsurance -> _uiState.update { it.copy(hasInsurance = !it.hasInsurance)}
        is RegisterEvent.UpdateAllergies -> _uiState.update { it.copy(allergies = event.allergies) }
        is RegisterEvent.UpdateConfirmPassword -> _uiState.update { it.copy(confirmPassword = event.confirmPassword) }
        is RegisterEvent.UpdateDateOfBirth -> _uiState.update { it.copy(dateOfBirth = event.dateOfBirth) }
        is RegisterEvent.UpdateEmail -> _uiState.update { it.copy(email = event.email) }
        is RegisterEvent.UpdateFullName -> _uiState.update { it.copy(fullName = event.fullName) }
        is RegisterEvent.UpdateGender -> _uiState.update { it.copy(genderIndex = event.genderIndex) }
        is RegisterEvent.UpdateNRIC -> _uiState.update { it.copy(nric = event.nric) }
        is RegisterEvent.UpdatePassword -> _uiState.update { it.copy(password = event.password) }
        is RegisterEvent.UpdatePhoneNo -> _uiState.update { it.copy(phoneNo = event.phoneNo) }
        is RegisterEvent.UpdateSpecialization -> _uiState.update { it.copy(specialization = event.specialization) }
    }
}