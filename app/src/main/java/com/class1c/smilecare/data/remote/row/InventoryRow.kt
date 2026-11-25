package com.class1c.smilecare.data.remote.row

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Row of the Supabase `Inventory` table.
 *
 * Columns: `amount`, `UnitPrice`, `InventoryName`.
 *
 * `UnitPrice` is a bigint column, so prices are stored as whole cents
 * (e.g. RM 12.50 is stored as 1250) and converted back when read.
 */
@Serializable
data class InventoryRow(
    val amount: Int,
    @SerialName("UnitPrice") val unitPrice: Long,
    @SerialName("InventoryName") val inventoryName: String,
)
