package com.class1c.smilecare.features.inventory.equipmentform.presentation

enum class EquipmentFormField {
    NAME, LOCATION
}

sealed interface EquipmentFormEvent {
    data class UpdateName(val name: String) : EquipmentFormEvent
    data class UpdateLocation(val location: String) : EquipmentFormEvent
    data class ValidateInput(val field: EquipmentFormField) : EquipmentFormEvent
    data object SaveEquipment : EquipmentFormEvent
}
