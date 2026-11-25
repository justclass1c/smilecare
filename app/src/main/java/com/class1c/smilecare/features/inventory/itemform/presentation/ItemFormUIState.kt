package com.class1c.smilecare.features.inventory.itemform.presentation

data class ItemFormUIState(
    val name: String = "",
    val quantity: String = "",
    val price: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,

    // errors:
    val invalidName: String? = null,
    val invalidQuantity: String? = null,
    val invalidPrice: String? = null,
)
