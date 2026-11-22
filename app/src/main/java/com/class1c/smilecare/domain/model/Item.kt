package com.class1c.smilecare.domain.model

/**
 * Consumable stock item used during treatment (e.g. gloves, composite resin).
 *
 * `quantity` is the current stock on hand, not the amount used in one visit.
 */
data class Item(
    val itemID: String,
    val itemName: String,
    val quantity: Int,
    val price: Double,
)
