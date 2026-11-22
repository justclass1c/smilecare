package com.class1c.smilecare.features.services.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.ui.components.MetricCard
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.OutlineButton
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileAlertDialog
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.StatusPill
import com.class1c.smilecare.ui.components.StatusTone

/**
 * Delete-service confirmation. The blocked variant explains why a service
 * used by invoices cannot be removed.
 */
@Composable
fun DeleteServiceScreen(
    blocked: Boolean = false,
    showDialog: Boolean = true,
    onBackClick: () -> Unit = {},
    onKeepClick: () -> Unit = {},
    onDeactivateClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    SmileScreen(
        title = "Delete service",
        onBackClick = onBackClick,
    ) {
        SmileCard {
            Text(
                text = "Service name",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            StatusPill(
                text = if (blocked) "Used in invoices" else "Active",
                tone = if (blocked) StatusTone.WARNING else StatusTone.SUCCESS,
            )
            Text(
                text = "Service description",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricCard(
                label = "Price estimate",
                value = "RM 00.00",
                hint = "Error text",
                modifier = Modifier.fillMaxWidth(),
            )
            MetricCard(
                label = "Treatment duration",
                value = "00 min",
                hint = "Error text",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        SectionTitle(title = "Materials used")
        SmileCard {
            Text(
                text = "Inventory item · x 0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        NoticeCard(
            text = "Invoice history is protected. Deactivate this service to stop new bookings without changing past records.",
            title = "Invoice history is protected",
            icon = Icons.Filled.MedicalServices,
        )

        OutlineButton(
            text = "Edit service",
            onClick = onBackClick,
        )
        OutlineButton(
            text = if (blocked) "Deactivate instead" else "Deactivate",
            onClick = onDeactivateClick,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Delete service",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Medium,
            )
        }
    }

    if (showDialog) {
        if (blocked) {
            SmileAlertDialog(
                title = "This service can't be deleted",
                message = "This service is used in existing invoices. Deactivate it instead.",
                confirmLabel = "Deactivate instead",
                dismissLabel = "Cancel",
                tone = StatusTone.WARNING,
                onConfirmClick = onDeactivateClick,
                onDismissClick = onKeepClick,
                extraContent = {
                    NoticeCard(
                        text = "Deactivating prevents new bookings while keeping past invoices and treatment records unchanged.",
                        title = "Keep invoice history intact",
                        tone = StatusTone.WARNING,
                    )
                },
            )
        } else {
            SmileAlertDialog(
                title = "Delete Service name?",
                message = "This removes the service from your catalog. This action cannot be undone.",
                confirmLabel = "Delete",
                dismissLabel = "Cancel",
                tone = StatusTone.DANGER,
                onConfirmClick = onDeleteClick,
                onDismissClick = onKeepClick,
                extraContent = {
                    NoticeCard(
                        text = "RM 00.00 · 00 min",
                        title = "Service name",
                    )
                },
            )
        }
    }
}
