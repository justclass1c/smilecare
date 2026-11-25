package com.class1c.smilecare.data.repository

import com.class1c.smilecare.data.remote.row.EquipmentInsertRow
import com.class1c.smilecare.data.remote.row.EquipmentRow
import com.class1c.smilecare.domain.model.Equipment
import com.class1c.smilecare.domain.repository.EquipmentRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Supabase implementation backed by the `Equipment` table
 * (`EquipmentName`, `Location`, `Status`).
 */
@Singleton
class EquipmentRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient
) : EquipmentRepository {

    override suspend fun listEquipment(): List<Equipment> {
        return supabase.from(TABLE)
            .select()
            .decodeList<EquipmentRow>()
            .map { row ->
                Equipment(
                    equipmentID = "",
                    equipmentName = row.equipmentName,
                    location = row.location,
                    status = row.status,
                )
            }
    }

    override suspend fun insertEquipment(equipment: Equipment) {
        supabase.from(TABLE).insert(
            EquipmentInsertRow(
                equipmentName = equipment.equipmentName,
                location = equipment.location,
            )
        )
    }

    override suspend fun updateEquipmentStatus(equipmentName: String, status: String) {
        supabase.from(TABLE).update(
            {
                set("Status", status)
            }
        ) {
            filter {
                eq("EquipmentName", equipmentName)
            }
        }
    }

    private companion object {
        const val TABLE = "Equipment"
    }
}
