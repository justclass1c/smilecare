package com.class1c.smilecare.domain.model

/**
 * Maintenance record for an equipment unit.
 *
 * Composite primary key: (equipmentID, dentistID).
 * Both fields are foreign keys: equipmentID -> Equipment, dentistID -> User.
 * The dentist is the staff member responsible for the maintenance.
 */
data class EquipmentMaintenance(
    val equipmentID: String,
    val dentistID: String,
    val nextMaintenanceDate: String,
    val lastMaintenanceDate: String,
)
