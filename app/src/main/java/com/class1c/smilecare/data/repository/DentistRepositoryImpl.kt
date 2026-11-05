package com.class1c.smilecare.data.repository

import com.class1c.smilecare.data.local.DentistLocalDataSource
import com.class1c.smilecare.data.mapper.toDomain
import com.class1c.smilecare.domain.model.Dentist
import com.class1c.smilecare.domain.repository.DentistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The only implementation of [DentistRepository].
 *
 * It depends on a single data source, so every consumer observes the same
 * records. That shared instance is the actual source of truth.
 */
@Singleton
class DentistRepositoryImpl @Inject constructor(
    private val localDataSource: DentistLocalDataSource,
) : DentistRepository {

    override fun observeDentists(): Flow<List<Dentist>> =
        localDataSource.observeDentists().map { entities -> entities.map { it.toDomain() } }

    override suspend fun setFavorite(dentistId: String, isFavorite: Boolean) {
        localDataSource.setFavorite(dentistId = dentistId, isFavorite = isFavorite)
    }
}
