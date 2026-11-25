package com.class1c.smilecare.features.inventory.inventorydetail.presentation

enum class InventoryItemDetailField {
    AMOUNT
}

sealed interface InventoryItemDetailEvent {
    data class UpdateAmount(val amount: String) : InventoryItemDetailEvent
    data class ValidateInput(val field: InventoryItemDetailField) : InventoryItemDetailEvent
    data object SaveItem : InventoryItemDetailEvent
    data object Reload : InventoryItemDetailEvent
    data object RequestDelete : InventoryItemDetailEvent
    data object DismissDelete : InventoryItemDetailEvent
    data object ConfirmDelete : InventoryItemDetailEvent
}
