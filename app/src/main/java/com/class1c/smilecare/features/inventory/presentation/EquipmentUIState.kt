package com.class1c.smilecare.features.inventory.presentation

import com.class1c.smilecare.domain.model.Equipment

data class EquipmentUIState(
    val items: List<Equipment> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
