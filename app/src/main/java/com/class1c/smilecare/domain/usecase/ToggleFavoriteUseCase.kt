package com.class1c.smilecare.domain.usecase

import com.class1c.smilecare.domain.model.Dentist
import com.class1c.smilecare.domain.repository.DentistRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: DentistRepository,
) {
    suspend operator fun invoke(dentistId: String) {
        val dentist: Dentist = repository.observeDentists()
            .first()
            .firstOrNull { it.id == dentistId }
            ?: return

        repository.setFavorite(
            dentistId = dentistId,
            isFavorite = !dentist.isFavorite,
        )
    }
}
