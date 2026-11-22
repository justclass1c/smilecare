package com.class1c.smilecare.features.inventory.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.navigation.Route
import com.class1c.smilecare.ui.components.ListRow
import com.class1c.smilecare.ui.components.MetricCard
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTabs
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusTone

/**
 * Clinic supply inventory. Stock levels are presentation placeholders; the
 * screen reports taps and tab changes to the caller.
 */
@Composable
fun InventoryScreen(
    selectedTab: Int = 0,
    onTabChange: (Int) -> Unit = {},
    onItemClick: (String) -> Unit = {},
    onAddClick: () -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    SmileScreen(
        role = UserRole.NURSE,
        currentRoute = Route.Inventory,
        title = "Inventory",
        onNavigate = onNavigate,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddClick,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                    )
                },
                text = { Text("Add item") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        },
    ) {
        Text(
            text = "Supplies and stock, kept ready for care.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SmileTextField(
            label = "Search inventory",
            value = "",
            placeholder = "Search",
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MetricCard(
                label = "Total items",
                value = "00",
                hint = "Error text",
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                label = "Low / out of stock",
                value = "00",
                hint = "Error text",
                modifier = Modifier.weight(1f),
            )
        }
        SmileTabs(
            options = listOf("All items", "Low stock"),
            selectedIndex = selectedTab,
            onSelect = onTabChange,
        )

        inventoryItems.forEach { item ->
            ListRow(
                icon = Icons.Filled.Inventory2,
                title = item.name,
                subtitle = item.subtitle,
                badgeText = item.status,
                badgeTone = item.tone,
                onClick = { onItemClick(item.id) },
            )
        }

        NoticeCard(
            text = "Stock levels are estimates. Update quantities after each treatment.",
        )
    }
}

private data class InventoryItem(
    val id: String,
    val name: String,
    val subtitle: String,
    val status: String,
    val tone: StatusTone,
)

private val inventoryItems = listOf(
    InventoryItem(
        id = "item-1",
        name = "Inventory item",
        subtitle = "00 boxes · Minimum 00",
        status = "In stock",
        tone = StatusTone.SUCCESS,
    ),
    InventoryItem(
        id = "item-2",
        name = "Inventory item",
        subtitle = "00 packs · Minimum 00",
        status = "Low stock",
        tone = StatusTone.WARNING,
    ),
    InventoryItem(
        id = "item-3",
        name = "Inventory item",
        subtitle = "00 boxes · Minimum 00",
        status = "In stock",
        tone = StatusTone.SUCCESS,
    ),
    InventoryItem(
        id = "item-4",
        name = "Inventory item",
        subtitle = "00 vials · Minimum 00",
        status = "Out of stock",
        tone = StatusTone.DANGER,
    ),
    InventoryItem(
        id = "item-5",
        name = "Inventory item",
        subtitle = "00 packs · Minimum 00",
        status = "In stock",
        tone = StatusTone.SUCCESS,
    ),
)
