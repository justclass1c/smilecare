package com.class1c.smilecare.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Temporary in-memory data source.
 *
 * This is the ONE place that owns dentist records, which is what keeps the
 * single-source-of-truth rule intact. Replace the MutableStateFlow with a Room
 * DAO later without touching the domain or presentation layers.
 */
@Singleton
class DentistLocalDataSource @Inject constructor() {

    private val dentists = MutableStateFlow(seedDentists)

    fun observeDentists(): Flow<List<DentistEntity>> = dentists.asStateFlow()

    fun setFavorite(dentistId: String, isFavorite: Boolean) {
        dentists.update { current ->
            current.map { dentist ->
                if (dentist.id == dentistId) dentist.copy(favorite = isFavorite) else dentist
            }
        }
    }

    private companion object {
        val seedDentists = listOf(
            DentistEntity(
                id = "1",
                fullName = "Dr. Aisha Rahman",
                specialty = "Orthodontics",
                rating = 4.9,
                experienceInYears = 12,
                favorite = true,
            ),
            DentistEntity(
                id = "2",
                fullName = "Dr. Marco Silva",
                specialty = "Pediatric Dentistry",
                rating = 4.7,
                experienceInYears = 8,
                favorite = false,
            ),
            DentistEntity(
                id = "3",
                fullName = "Dr. Nadia Okafor",
                specialty = "Cosmetic Dentistry",
                rating = 4.8,
                experienceInYears = 10,
                favorite = false,
            ),
        )
    }
}
