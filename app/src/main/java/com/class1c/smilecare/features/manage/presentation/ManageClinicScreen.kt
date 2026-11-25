package com.class1c.smilecare.features.manage.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.navigation.Route
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.QuickActionCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.StatusTone

/**
 * Reception management hub. Every tile is a navigation event; counts are
 * placeholder presentation data.
 */
@Composable
fun ManageClinicScreen(
    inventoryCount: Int = 0,
    equipmentCount: Int = 0,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onUsersClick: () -> Unit = {},
    onServicesClick: () -> Unit = {},
    onInventoryClick: () -> Unit = {},
    onEquipmentClick: () -> Unit = {},
    onLuckyDrawClick: () -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    SmileScreen(
        role = UserRole.NURSE,
        currentRoute = Route.ManageClinic,
        title = "Manage clinic",
        onNavigate = onNavigate,
    ) {
        Text(
            text = "Everything your clinic needs, in one place.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (errorMessage != null) {
            NoticeCard(
                text = errorMessage,
                title = "Could not load clinic totals",
                tone = StatusTone.DANGER,
            )
        }

        val cards = listOf(
            ManageCard(
                title = "Users",
                subtitle = "Staff & access · 0 staff",
                icon = Icons.Filled.People,
                onClick = onUsersClick,
            ),
            ManageCard(
                title = "Services",
                subtitle = "Your service catalog · 0 services",
                icon = Icons.Filled.MedicalServices,
                onClick = onServicesClick,
            ),
            ManageCard(
                title = "Inventory",
                subtitle = if (isLoading) {
                    "Supplies & stock · loading..."
                } else {
                    "Supplies & stock · $inventoryCount items"
                },
                icon = Icons.Filled.Inventory2,
                onClick = onInventoryClick,
            ),
            ManageCard(
                title = "Equipment",
                subtitle = if (isLoading) {
                    "Care for your tools · loading..."
                } else {
                    "Care for your tools · $equipmentCount assets"
                },
                icon = Icons.Filled.Build,
                onClick = onEquipmentClick,
            ),
            ManageCard(
                title = "Lucky Draw",
                subtitle = "A reason to smile · 0 active",
                icon = Icons.Filled.CardGiftcard,
                onClick = onLuckyDrawClick,
            ),
        )

        cards.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                row.forEach { card ->
                    QuickActionCard(
                        icon = card.icon,
                        title = card.title,
                        subtitle = card.subtitle,
                        onClick = card.onClick,
                        modifier = Modifier.weight(1f),
                    )
                }
                if (row.size == 1) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }

        NoticeCard(
            text = "Clinic-wide settings are limited to reception accounts.",
        )
    }
}

private data class ManageCard(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)
