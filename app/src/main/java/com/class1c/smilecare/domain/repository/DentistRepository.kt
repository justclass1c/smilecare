package com.class1c.smilecare.domain.repository

import com.class1c.smilecare.domain.model.Dentist
import kotlinx.coroutines.flow.Flow

/**
 * The single source of truth contract for dentists.
 *
 * The domain layer only knows this interface. The implementation lives in the
 * data layer and is provided once by [com.class1c.smilecare.di.AppContainer].
 */
interface DentistRepository {

    /** Emits the current dentist list and every later change. */
    fun observeDentists(): Flow<List<Dentist>>

    /** Marks or unmarks a dentist as favorite. */
    suspend fun setFavorite(dentistId: String, isFavorite: Boolean)
}
