package com.class1c.smilecare.features.inventory.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.Item
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.navigation.Route
import com.class1c.smilecare.ui.components.EmptyState
import com.class1c.smilecare.ui.components.ListRow
import com.class1c.smilecare.ui.components.MetricCard
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTabs
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.TextAction

/** Stock below this number is flagged as low. */
private const val LOW_STOCK_THRESHOLD = 50

/**
 * Clinic supply inventory. The rows come from the `Inventory` table through
 * [InventoryViewModel]; taps and tab changes are reported to the caller.
 */
@Composable
fun InventoryScreen(
    items: List<Item> = emptyList(),
    isLoading: Boolean = false,
    errorMessage: String? = null,
    selectedTab: Int = 0,
    onTabChange: (Int) -> Unit = {},
    onItemClick: (String) -> Unit = {},
    onAddClick: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }

    val visibleItems = items
        .filter { it.itemName.contains(searchQuery, ignoreCase = true) }
        .let { filtered ->
            if (selectedTab == 1) {
                filtered.filter { it.quantity < LOW_STOCK_THRESHOLD }
            } else {
                filtered
            }
        }

    SmileScreen(
        role = UserRole.NURSE,
        currentRoute = Route.Inventory,
        title = "Inventory",
        onBackClick = onBackClick,
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
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = "Search",
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MetricCard(
                label = "Total items",
                value = items.size.toString().padStart(2, '0'),
                hint = if (isLoading) "Loading" else "In the register",
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                label = "Low / out of stock",
                value = items.count { it.quantity < LOW_STOCK_THRESHOLD }
                    .toString()
                    .padStart(2, '0'),
                hint = "Needs restocking",
                modifier = Modifier.weight(1f),
            )
        }
        SmileTabs(
            options = listOf("All items", "Low stock"),
            selectedIndex = selectedTab,
            onSelect = onTabChange,
        )

        if (isLoading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Loading inventory...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else if (errorMessage != null) {
            NoticeCard(
                text = errorMessage,
                title = "Could not load inventory",
                tone = StatusTone.DANGER,
            )
            TextAction(
                text = "Try again",
                onClick = onRetryClick,
            )
        } else if (visibleItems.isEmpty()) {
            val hasItems = items.isNotEmpty()
            EmptyState(
                title = if (hasItems) "Nothing matches" else "No items yet",
                message = if (hasItems) {
                    "Try a different search or switch tabs."
                } else {
                    "Add your first inventory item to start tracking stock."
                },
                icon = Icons.Filled.Inventory2,
                actionLabel = if (hasItems) null else "Add item",
                onActionClick = onAddClick,
            )
        } else {
            visibleItems.forEach { item ->
                ListRow(
                    icon = Icons.Filled.Inventory2,
                    title = item.itemName,
                    subtitle = "${item.quantity} units · RM %.2f".format(item.price),
                    badgeText = stockLabel(item.quantity),
                    badgeTone = stockTone(item.quantity),
                    onClick = { onItemClick(item.itemName) },
                )
            }
        }

        NoticeCard(
            text = "Stock levels are estimates. Update quantities after each treatment.",
        )
    }
}

private fun stockLabel(quantity: Int): String = when {
    quantity <= 0 -> "Out of stock"
    quantity < LOW_STOCK_THRESHOLD -> "Low stock"
    else -> "In stock"
}

private fun stockTone(quantity: Int): StatusTone = when {
    quantity <= 0 -> StatusTone.DANGER
    quantity < LOW_STOCK_THRESHOLD -> StatusTone.WARNING
    else -> StatusTone.SUCCESS
}
