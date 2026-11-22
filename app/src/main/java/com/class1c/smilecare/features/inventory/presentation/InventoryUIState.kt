package com.class1c.smilecare.features.inventory.presentation

import com.class1c.smilecare.domain.model.Item

data class InventoryUIState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
