package com.class1c.smilecare.features.auth.login.presentation

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginSuccessful: Boolean = false,
    val loginError: String? = null
)