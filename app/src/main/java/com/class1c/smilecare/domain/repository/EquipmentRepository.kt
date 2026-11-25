package com.class1c.smilecare.domain.repository

import com.class1c.smilecare.domain.model.Equipment

interface EquipmentRepository {
    suspend fun listEquipment(): List<Equipment>

    suspend fun insertEquipment(equipment: Equipment)

    suspend fun updateEquipmentStatus(equipmentName: String, status: String)
}
