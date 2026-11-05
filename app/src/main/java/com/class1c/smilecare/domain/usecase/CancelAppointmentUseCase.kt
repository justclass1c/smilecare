package com.class1c.smilecare.domain.usecase

import com.class1c.smilecare.domain.repository.AppointmentRepository
import javax.inject.Inject

class CancelAppointmentUseCase @Inject constructor(
    private val repository: AppointmentRepository,
) {
    suspend operator fun invoke(appointmentId: String) {
        repository.cancelAppointment(appointmentId)
    }
}
