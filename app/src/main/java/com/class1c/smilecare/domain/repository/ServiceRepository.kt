package com.class1c.smilecare.domain.repository

import com.class1c.smilecare.domain.model.Service

interface ServiceRepository {
    suspend fun insertService(service: Service)

    suspend fun modifyService(service: Service)

    suspend fun listServices(): List<Service>

    suspend fun deleteService(service: Service)
}