package com.class1c.smilecare.features.inventory.equipmentdetail.presentation

sealed interface EquipmentDetailEvent {
    data class SelectStatus(val status: String) : EquipmentDetailEvent
    data object SaveStatus : EquipmentDetailEvent
    data object Reload : EquipmentDetailEvent
}
