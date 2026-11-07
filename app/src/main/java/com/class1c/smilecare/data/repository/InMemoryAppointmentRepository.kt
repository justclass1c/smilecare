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
