package com.class1c.smilecare.features.services.servicedetail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.OutlineButton
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.StatusPill
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.TextAction

/**
 * Service detail. Patients can book, dentists see the reference information,
 * reception sees materials and management actions.
 */
@Composable
fun ServiceDetailScreen(
    role: UserRole,
    onBackClick: () -> Unit = {},
    onBookClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onToggleActiveClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    SmileScreen(
        title = "Service details",
        onBackClick = onBackClick,
    ) {
        SmileCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.MedicalServices,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusPill(text = "Check-up", tone = StatusTone.INFO)
                        if (role == UserRole.NURSE) {
                            StatusPill(
                                text = "Active",
                                tone = StatusTone.SUCCESS,
                            )
                        }
                    }
                    Text(
                        text = "Service name",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            Text(
                text = "Service description",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MetricBlock(
                label = "Price estimate",
                value = "RM 00.00",
                modifier = Modifier.weight(1f),
            )
            MetricBlock(
                label = "Treatment duration",
                value = "00 min",
                modifier = Modifier.weight(1f),
            )
        }

        SectionTitle(title = "About this service")
        Text(
            text = "Error text",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        SectionTitle(title = "What to expect")
        Expectation(text = "Error text")
        Expectation(text = "Error text")
        Expectation(text = "Error text")

        when (role) {
            UserRole.PATIENT -> {
                NoticeCard(
                    text = "Fees and treatment time are estimates. Your dentist will discuss your care plan before treatment.",
                )
                PrimaryButton(
                    text = "Book this service",
                    onClick = onBookClick,
                    leadingIcon = Icons.Filled.CalendarMonth,
                )
                Text(
                    text = "Appointments use one-hour slots. Your dentist will advise if follow-up visits are needed.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            UserRole.DENTIST -> {
                NoticeCard(
                    text = "Fees and treatment time are estimates and may vary with the care plan.",
                )
            }

            else -> {
                SectionTitle(title = "Materials used")
                MaterialsRow(item = "Inventory item", quantity = "x 0")
                MaterialsRow(item = "Inventory item", quantity = "x 0")
                NoticeCard(
                    text = "Invoice history is protected. Deactivate this service to stop new bookings without changing past records.",
                    title = "Invoice history is protected",
                )
                OutlineButton(
                    text = "Edit service",
                    onClick = onEditClick,
                    leadingIcon = Icons.Filled.Edit,
                )
                OutlineButton(
                    text = "Deactivate",
                    onClick = onToggleActiveClick,
                )
                TextAction(
                    text = "Delete service",
                    onClick = onDeleteClick,
                    color = MaterialTheme.colorScheme.error,
                    icon = Icons.Filled.Delete,
                )
            }
        }
    }
}

@Composable
private fun MetricBlock(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    SmileCard(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun Expectation(text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun MaterialsRow(item: String, quantity: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Inventory2,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = item,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
        )
        StatusPill(text = quantity, tone = StatusTone.NEUTRAL)
    }
}
