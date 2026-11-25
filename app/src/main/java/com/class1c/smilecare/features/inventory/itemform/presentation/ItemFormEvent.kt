package com.class1c.smilecare.features.inventory.itemform.presentation

enum class ItemFormField {
    NAME, QUANTITY, PRICE
}

sealed interface ItemFormEvent {
    data class UpdateName(val name: String) : ItemFormEvent
    data class UpdateQuantity(val quantity: String) : ItemFormEvent
    data class UpdatePrice(val price: String) : ItemFormEvent
    data class ValidateInput(val field: ItemFormField) : ItemFormEvent
    data object SaveItem : ItemFormEvent
}
