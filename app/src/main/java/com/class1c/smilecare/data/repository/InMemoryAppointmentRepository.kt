package com.class1c.smilecare.data.repository

import com.class1c.smilecare.domain.model.Appointment
import com.class1c.smilecare.domain.model.AppointmentStatus
import com.class1c.smilecare.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In-memory starter implementation. Replace with a Room/Retrofit-backed version later
 * without touching the domain or the feature layer.
 */
@Singleton
class InMemoryAppointmentRepository @Inject constructor() : AppointmentRepository {

    private val appointments = MutableStateFlow(
        listOf(
            Appointment("1", "Dr. Maya Patel", "Nov 12, 2026", "09:30", AppointmentStatus.UPCOMING),
            Appointment("2", "Dr. Liam Chen", "Nov 18, 2026", "14:00", AppointmentStatus.UPCOMING),
            Appointment("3", "Dr. Maya Patel", "Oct 02, 2026", "11:15", AppointmentStatus.COMPLETED),
        ),
    )

    override fun observeAppointments(): Flow<List<Appointment>> = appointments.asStateFlow()

    override suspend fun cancelAppointment(id: String) {
        appointments.update { current ->
            current.map { appointment ->
                if (appointment.id == id) {
                    appointment.copy(status = AppointmentStatus.CANCELLED)
                } else {
                    appointment
                }
            }
        }
    }
}
