package com.class1c.smilecare.features.inventory.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
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
 * Equipment register with maintenance states. Tapping an asset reports its id
 * to the caller; no records are owned here.
 */
@Composable
fun EquipmentScreen(
    selectedTab: Int = 0,
    onTabChange: (Int) -> Unit = {},
    onEquipmentClick: (String) -> Unit = {},
    onAddClick: () -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    SmileScreen(
        role = UserRole.NURSE,
        currentRoute = Route.Equipment,
        title = "Equipment",
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
            value = "",
            placeholder = "Search",
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MetricCard(
                label = "Total equipment",
                value = "00",
                hint = "Error text",
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                label = "Needs attention",
                value = "00",
                hint = "Error text",
                modifier = Modifier.weight(1f),
            )
        }
        SmileTabs(
            options = listOf("All items", "Maintenance"),
            selectedIndex = selectedTab,
            onSelect = onTabChange,
        )

        equipmentItems.forEach { item ->
            ListRow(
                icon = Icons.Filled.Build,
                title = item.name,
                subtitle = item.subtitle,
                badgeText = item.status,
                badgeTone = item.tone,
                onClick = { onEquipmentClick(item.id) },
            )
        }

        NoticeCard(
            text = "Overdue maintenance should be scheduled with the clinic's service partner.",
        )
    }
}

private data class EquipmentItem(
    val id: String,
    val name: String,
    val subtitle: String,
    val status: String,
    val tone: StatusTone,
)

private val equipmentItems = listOf(
    EquipmentItem(
        id = "equipment-1",
        name = "Equipment name",
        subtitle = "Treatment Room 1",
        status = "Operational",
        tone = StatusTone.SUCCESS,
    ),
    EquipmentItem(
        id = "equipment-2",
        name = "Equipment name",
        subtitle = "Service due · DD/MM/YY",
        status = "Due soon",
        tone = StatusTone.WARNING,
    ),
    EquipmentItem(
        id = "equipment-3",
        name = "Equipment name",
        subtitle = "Service due · DD/MM/YY",
        status = "Overdue",
        tone = StatusTone.DANGER,
    ),
    EquipmentItem(
        id = "equipment-4",
        name = "Equipment name",
        subtitle = "Treatment Room 2",
        status = "Under repair",
        tone = StatusTone.DANGER,
    ),
)
