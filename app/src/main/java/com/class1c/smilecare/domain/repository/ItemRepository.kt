package com.class1c.smilecare.domain.repository

import com.class1c.smilecare.domain.model.Item

interface ItemRepository {
    suspend fun listItems(): List<Item>

    suspend fun insertItem(item: Item)

    suspend fun updateItemAmount(itemName: String, quantity: Int)

    suspend fun deleteItem(itemName: String)
}
