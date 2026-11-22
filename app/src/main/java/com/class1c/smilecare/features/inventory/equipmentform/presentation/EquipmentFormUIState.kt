package com.class1c.smilecare.features.inventory.equipmentform.presentation

/** Locations an asset can be assigned to. */
val EQUIPMENT_LOCATIONS: List<String> = listOf(
    "Treatment room 1",
    "Treatment room 2",
)

data class EquipmentFormUIState(
    val name: String = "",
    val location: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,

    // errors:
    val invalidName: String? = null,
    val invalidLocation: String? = null,
)
