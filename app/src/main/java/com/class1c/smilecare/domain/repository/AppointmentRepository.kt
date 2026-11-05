package com.class1c.smilecare.domain.repository

import com.class1c.smilecare.domain.model.Appointment
import kotlinx.coroutines.flow.Flow

/**
 * The domain owns this contract. The data layer implements it, features depend on it.
 * Depending on the interface (not the implementation) is what keeps a single source of truth.
 */
interface AppointmentRepository {

    fun observeAppointments(): Flow<List<Appointment>>

    suspend fun cancelAppointment(id: String)
}
