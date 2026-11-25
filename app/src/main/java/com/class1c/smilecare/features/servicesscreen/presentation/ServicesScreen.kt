package com.class1c.smilecare.features.servicesscreen.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.navigation.Route
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusPill
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.SuccessBanner

/**
 * Service catalogue. Patients browse and book, dentists use it as price
 * reference, reception manages active/inactive entries.
 */
@Composable
fun ServicesScreen(
    role: UserRole,
    state: ServicesScreenUIState,
    selectedCategory: Int = 0,
    showSuccess: Boolean = false,
    onCategoryChange: (Int) -> Unit = {},
    onServiceClick: (String) -> Unit = {},
    onAddServiceClick: () -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    val services = state.services.orEmpty() // orEmpty is null check for lists

    SmileScreen(
        role = role,
        currentRoute = Route.Services,
        title = when (role) {
            UserRole.PATIENT -> "Our Services"
            UserRole.DENTIST -> "Service Catalog"
            else -> "Manage Services"
        },
        onNavigate = onNavigate,
        floatingActionButton = {
            if (role == UserRole.NURSE) {
                ExtendedFloatingActionButton(
                    onClick = onAddServiceClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                        )
                    },
                    text = { Text("Add service") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
    ) {
        Text(
            text = when (role) {
                UserRole.PATIENT -> "Care for every smile, at every stage."
                UserRole.DENTIST -> "Services and pricing at a glance."
                else -> "Keep the catalogue accurate and current."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SmileTextField(
            label = "Search services",
            value = "",
            placeholder = "Search",
        )
        SectionTitle(
            title = "Available services",
            actionLabel = "${services.size}",
        )

        services.forEach { service ->
            SmileCard(onClick = { onServiceClick(service.serviceID) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = service.serviceName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (role == UserRole.NURSE) {
                        StatusPill(
                            text = if (service.isActive) "Active" else "Inactive",
                            tone = if (service.isActive) StatusTone.SUCCESS else StatusTone.NEUTRAL,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Text(
                    text = service.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "RM %.2f".format(service.price),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = service.duration.toString() + " minutes",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        if (showSuccess) {
            SuccessBanner("Service saved successfully")
        }

        if (role == UserRole.PATIENT) {
            NoticeCard(
                text = "Fees and treatment time are estimates. Your dentist will discuss your care plan before treatment.",
            )
        }
    }
}

private fun serviceIcon(category: String): ImageVector = when (category) {
    "Cleaning" -> Icons.Filled.Spa
    "Filling" -> Icons.Filled.Healing
    "Extraction" -> Icons.Filled.MedicalServices
    "Root Canal" -> Icons.Filled.Healing
    "Whitening" -> Icons.Filled.AutoAwesome
    "Orthodontics" -> Icons.Filled.Favorite
    else -> Icons.Filled.HealthAndSafety
}

private val serviceCategories = listOf(
    "All",
    "Check-up",
    "Cleaning",
    "Filling",
    "Extraction",
    "Root Canal",
    "Whitening",
    "Orthodontics",
)

