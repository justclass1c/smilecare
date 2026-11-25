package com.class1c.smilecare.features.appointments.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.ui.components.Avatar
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusPill
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.SuccessBanner

/**
 * Dentist completion form. Selected services and notes are hoisted, and the
 * success/error variants share the same destination.
 */
@Composable
fun CompleteAppointmentScreen(
    selectedServices: List<String> = listOf("Service name"),
    onServiceToggle: (String) -> Unit = {},
    showError: Boolean = false,
    isComplete: Boolean = false,
    onBackClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
) {
    SmileScreen(
        title = "Complete appointment",
        onBackClick = onBackClick,
    ) {
        if (isComplete) {
            SuccessBanner("Appointment completed. Sent to reception for invoicing.")
            SmileCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Avatar(initials = "PN", size = 48.dp)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Patient name",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "DD/MM/YY · 00:00 AM",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    StatusPill(text = "Completed", tone = StatusTone.SUCCESS)
                }
            }
            SectionTitle(title = "Services rendered")
            SmileCard {
                Text(
                    text = "Service name · Service name",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            SectionTitle(title = "Clinical notes")
            SmileCard {
                Text(
                    text = "Error text",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            NoticeCard(
                text = "Reception will prepare the invoice from this visit.",
                tone = StatusTone.SUCCESS,
                icon = Icons.Filled.Check,
            )
            PrimaryButton(text = "Back to schedule", onClick = onBackClick)
            return@SmileScreen
        }

        SmileCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Avatar(initials = "PN", size = 48.dp)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = "Patient name",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "DD/MM/YY · 00:00 AM",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                StatusPill(text = "Scheduled", tone = StatusTone.INFO)
            }
        }

        Text(
            text = "Every detail helps us care.",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "Select all services rendered during this visit.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (showError) {
            NoticeCard(
                text = "Please check the highlighted details below.",
                title = "We couldn't complete this appointment",
                tone = StatusTone.DANGER,
            )
        }

        SectionTitle(title = "Services rendered *")
        serviceOptions.forEach { service ->
            SelectableServiceRow(
                name = service,
                selected = selectedServices.contains(service),
                onClick = { onServiceToggle(service) },
            )
        }
        if (showError) {
            Text(
                text = "Select at least one service",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
            )
        }

        SmileTextField(
            label = "Clinical notes",
            value = "",
            placeholder = "Treatment, findings, and follow-up advice",
            singleLine = false,
            minLines = 3,
        )

        SmileCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Catalog total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "RM 00.00",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            PrimaryButton(text = "Mark as completed", onClick = onConfirmClick)
            Text(
                text = "Reception will receive this visit for invoicing.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SelectableServiceRow(
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            },
        ),
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.MedicalServices,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Column(Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "RM 00.00 · 00 min",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Checkbox(checked = selected, onCheckedChange = { onClick() })
        }
    }
}

private val serviceOptions = listOf(
    "Service name",
    "Service name",
    "Service name",
)
