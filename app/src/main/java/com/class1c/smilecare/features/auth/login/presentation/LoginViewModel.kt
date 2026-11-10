package com.class1c.smilecare.features.auth.login.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun onEvent(event: LoginEvent) = when(event) {
        is LoginEvent.UpdateEmail -> _uiState.update { it.copy(email = event.email) }
        is LoginEvent.UpdatePassword -> _uiState.update { it.copy(password = event.password) }
        LoginEvent.Login -> TODO()
    }
}