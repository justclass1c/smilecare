package com.class1c.smilecare.features.auth.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.class1c.smilecare.di.UserSession
import com.class1c.smilecare.domain.model.user.User
import com.class1c.smilecare.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userSession: UserSession
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun onEvent(event: LoginEvent) = when(event) {
        is LoginEvent.UpdateEmail -> _uiState.update { it.copy(email = event.email) }
        is LoginEvent.UpdatePassword -> _uiState.update { it.copy(password = event.password) }
        LoginEvent.Login -> {
            _uiState.update { it.copy(isLoading = true) }
            viewModelScope.launch {
                runCatching {
                    userRepository.loginUser(_uiState.value.email, _uiState.value.password)
                }.onSuccess { user -> // user gets the result from loginUser method call
                    // if not found, then credentials are incorrect
                    if (user == null) _uiState.update { it.copy(loginSuccessful = false, isLoading = false, loginError = "Incorrect email or password") }
                    else {
                        _uiState.update { it.copy(loginSuccessful = true, isLoading = false) }
                        userSession.setCurrentUser(user)
                    }
                }.onFailure { e -> _uiState.update { it.copy(isLoading = false, loginError = "Something went wrong. Ensure your network connection is stable and try again.") } }
            }

        }
    }
}