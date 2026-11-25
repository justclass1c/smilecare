package com.class1c.smilecare.features.inventory.equipmentdetail.presentation

import com.class1c.smilecare.domain.model.STATUS_OPERATIONAL

data class EquipmentDetailUIState(
    val equipmentName: String = "",
    val location: String = "",
    val status: String = STATUS_OPERATIONAL,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
)
