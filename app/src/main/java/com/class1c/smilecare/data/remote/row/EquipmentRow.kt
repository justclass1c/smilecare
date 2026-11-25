package com.class1c.smilecare.data.remote.row

import com.class1c.smilecare.domain.model.STATUS_OPERATIONAL
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Row of the Supabase `Equipment` table.
 *
 * Columns: `EquipmentName`, `Location`, `Status`.
 * `Status` has a default so rows still decode if the column is absent.
 */
@Serializable
data class EquipmentRow(
    @SerialName("EquipmentName") val equipmentName: String,
    @SerialName("Location") val location: String,
    @SerialName("Status") val status: String = STATUS_OPERATIONAL,
)

/** Insert payload without `Status`, so the column default applies. */
@Serializable
data class EquipmentInsertRow(
    @SerialName("EquipmentName") val equipmentName: String,
    @SerialName("Location") val location: String,
)
