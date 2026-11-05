package com.class1c.smilecare.domain.usecase

import com.class1c.smilecare.domain.model.Dentist
import com.class1c.smilecare.domain.repository.DentistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * One use case = one user-facing action.
 *
 * ViewModels depend on use cases, never on repositories or data sources, which
 * keeps the presentation layer free of data-layer details.
 */
class ObserveDentistsUseCase @Inject constructor(
    private val repository: DentistRepository,
) {
    operator fun invoke(): Flow<List<Dentist>> = repository.observeDentists()
}
