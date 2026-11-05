package com.class1c.smilecare.domain.usecase

import com.class1c.smilecare.domain.model.Appointment
import com.class1c.smilecare.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** One action per class, invoked with `operator fun invoke`. */
class GetAppointmentsUseCase @Inject constructor(
    private val repository: AppointmentRepository,
) {
    operator fun invoke(): Flow<List<Appointment>> = repository.observeAppointments()
}
