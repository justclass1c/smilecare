package com.class1c.smilecare.features.auth.login.presentation

sealed interface LoginEvent {
    data class UpdateEmail(val email: String): LoginEvent
    data class UpdatePassword(val password: String): LoginEvent
    data object Login: LoginEvent
}