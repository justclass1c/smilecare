package com.class1c.smilecare.features.dentists.presentation

import com.class1c.smilecare.domain.model.Dentist

/**
 * Everything the screen needs to render. The ViewModel is the only writer.
 */
data class DentistsUiState(
    val isLoading: Boolean = true,
    val dentists: List<Dentist> = emptyList(),
    val errorMessage: String? = null,
)
