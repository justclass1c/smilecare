package com.class1c.smilecare.features.user.presentation

sealed interface UsersEvent {
    data class SwitchTab(val index: Int): UsersEvent
    data class UpdateSearchQuery(val query: String): UsersEvent
    data object ViewButton: UsersEvent
}