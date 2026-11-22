package com.class1c.smilecare.features.inventory.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
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
import com.class1c.smilecare.domain.model.Equipment
import com.class1c.smilecare.domain.model.STATUS_DUE_SOON
import com.class1c.smilecare.domain.model.STATUS_OVERDUE
import com.class1c.smilecare.domain.model.STATUS_UNDER_REPAIR
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

/**
 * Equipment register backed by the `Equipment` table. Tapping an asset reports
 * its id to the caller; no records are owned here.
 */
@Composable
fun EquipmentScreen(
    items: List<Equipment> = emptyList(),
    isLoading: Boolean = false,
    errorMessage: String? = null,
    selectedTab: Int = 0,
    onTabChange: (Int) -> Unit = {},
    onEquipmentClick: (String) -> Unit = {},
    onAddClick: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }

    val visibleItems = items.filter { equipment ->
        equipment.equipmentName.contains(searchQuery, ignoreCase = true) ||
            equipment.location.contains(searchQuery, ignoreCase = true)
    }

    SmileScreen(
        role = UserRole.NURSE,
        currentRoute = Route.Equipment,
        title = "Equipment",
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
                text = { Text("Add asset") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        },
    ) {
        Text(
            text = "Care for the tools that care for smiles.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SmileTextField(
            label = "Search equipment",
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = "Search",
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MetricCard(
                label = "Total equipment",
                value = items.size.toString().padStart(2, '0'),
                hint = if (isLoading) "Loading" else "In the register",
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                label = "Rooms in use",
                value = items.map { it.location }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .size
                    .toString()
                    .padStart(2, '0'),
                hint = "Distinct locations",
                modifier = Modifier.weight(1f),
            )
        }
        SmileTabs(
            options = listOf("All items", "Maintenance"),
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
                    text = "Loading equipment...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else if (errorMessage != null) {
            NoticeCard(
                text = errorMessage,
                title = "Could not load equipment",
                tone = StatusTone.DANGER,
            )
            TextAction(
                text = "Try again",
                onClick = onRetryClick,
            )
        } else if (selectedTab == 1) {
            EmptyState(
                title = "Nothing scheduled",
                message = "Maintenance records are not tracked yet.",
                icon = Icons.Filled.Build,
            )
        } else if (visibleItems.isEmpty()) {
            val hasItems = items.isNotEmpty()
            EmptyState(
                title = if (hasItems) "Nothing matches" else "No equipment yet",
                message = if (hasItems) {
                    "Try a different search."
                } else {
                    "Add your first asset to start the register."
                },
                icon = Icons.Filled.Build,
                actionLabel = if (hasItems) null else "Add asset",
                onActionClick = onAddClick,
            )
        } else {
            visibleItems.forEach { equipment ->
                ListRow(
                    icon = Icons.Filled.Build,
                    title = equipment.equipmentName,
                    subtitle = equipment.location.ifBlank { "No location set" },
                    badgeText = equipment.status,
                    badgeTone = equipmentStatusTone(equipment.status),
                    onClick = { onEquipmentClick(equipment.equipmentName) },
                )
            }
        }

        NoticeCard(
            text = "Overdue maintenance should be scheduled with the clinic's service partner.",
        )
    }
}

private fun equipmentStatusTone(status: String): StatusTone = when (status) {
    STATUS_OVERDUE -> StatusTone.DANGER
    STATUS_DUE_SOON -> StatusTone.WARNING
    STATUS_UNDER_REPAIR -> StatusTone.INFO
    else -> StatusTone.SUCCESS
}
