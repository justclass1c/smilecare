package com.class1c.smilecare.data.repository

import com.class1c.smilecare.data.remote.row.InventoryRow
import com.class1c.smilecare.domain.model.Item
import com.class1c.smilecare.domain.repository.ItemRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToLong

/**
 * Supabase implementation backed by the `Inventory` table
 * (`amount`, `UnitPrice`, `InventoryName`).
 */
@Singleton
class InventoryRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient
) : ItemRepository {

    override suspend fun listItems(): List<Item> {
        return supabase.from(TABLE)
            .select(Columns.list("InventoryName", "amount", "UnitPrice"))
            .decodeList<InventoryRow>()
            .map { row ->
                Item(
                    itemID = "",
                    itemName = row.inventoryName,
                    quantity = row.amount,
                    price = row.unitPrice / 100f,
                )
            }
    }

    override suspend fun insertItem(item: Item) {
        supabase.from(TABLE).insert(
            InventoryRow(
                amount = item.quantity,
                unitPrice = (item.price * 100f).roundToLong(),
                inventoryName = item.itemName,
            )
        )
    }

    override suspend fun updateItemAmount(itemName: String, quantity: Int) {
        supabase.from(TABLE).update(
            {
                set("amount", quantity)
            }
        ) {
            filter {
                eq("InventoryName", itemName)
            }
        }
    }

    override suspend fun deleteItem(itemName: String) {
        supabase.from(TABLE).delete {
            filter {
                eq("InventoryName", itemName)
            }
        }
    }

    private companion object {
        const val TABLE = "Inventory"
    }
}
