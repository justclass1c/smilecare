package com.class1c.smilecare.features.dentists.presentation

/**
 * User intents flowing up from the UI. The screen never changes state directly.
 */
sealed interface DentistsUiEvent {

    data class OnFavoriteToggled(val dentistId: String) : DentistsUiEvent

    data object OnRetryClicked : DentistsUiEvent

    data object OnErrorShown : DentistsUiEvent
}
