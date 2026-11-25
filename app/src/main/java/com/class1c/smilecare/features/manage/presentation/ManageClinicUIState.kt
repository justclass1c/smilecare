package com.class1c.smilecare.features.manage.presentation

data class ManageClinicUIState(
    val inventoryCount: Int = 0,
    val equipmentCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
