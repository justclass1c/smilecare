package com.class1c.smilecare.features.inventory.inventorydetail.presentation

data class InventoryItemDetailUIState(
    val itemName: String = "",
    val amount: String = "",
    val price: Float = 0f,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val errorMessage: String? = null,

    // errors:
    val invalidAmount: String? = null,
)
