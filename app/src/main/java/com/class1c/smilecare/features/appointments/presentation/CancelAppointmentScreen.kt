package com.class1c.smilecare.features.appointments.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.ui.components.DetailRow
import com.class1c.smilecare.ui.components.HoursNote
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileAlertDialog
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusPill
import com.class1c.smilecare.ui.components.StatusTone

/**
 * Cancellation screen. The confirmation dialog is controlled by [showDialog]
 * so the caller decides when the prompt is open.
 */
@Composable
fun CancelAppointmentScreen(
    role: UserRole = UserRole.PATIENT,
    showDialog: Boolean = true,
    reason: String = "",
    onReasonChange: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    
    onKeepClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
) {
    val nurses = role == UserRole.NURSE
    SmileScreen(
        title = if (nurses) "Cancel appointment" else "Cancel appointment",
        onBackClick = onBackClick,
    ) {
        SmileCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "APT-0000",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                StatusPill(text = "Scheduled", tone = StatusTone.INFO)
            }
            Text(
                text = "Service name",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Patient name",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        SectionTitle(title = "Appointment details")
        SmileCard {
            DetailRow(label = "Dentist", value = "Dentist name")
            DetailRow(label = "Date", value = "DD/MM/YY")
            DetailRow(label = "Time", value = "00:00 AM – 00:00 PM")
            DetailRow(label = "Price estimate", value = "RM 00.00")
        }

        NoticeCard(
            text = "Cancelling frees this slot for another patient. Your appointment history stays intact.",
        )
        HoursNote()
    }

    if (showDialog) {
        SmileAlertDialog(
            title = "Cancel this appointment?",
            message = "The record will be kept as Cancelled.",
            confirmLabel = "Cancel appointment",
            dismissLabel = "Keep appointment",
            tone = StatusTone.DANGER,
            onConfirmClick = onConfirmClick,
            onDismissClick = onKeepClick,
            extraContent = {
                SmileTextField(
                    label = "Reason (optional)",
                    value = reason,
                    onValueChange = onReasonChange,
                    placeholder = "Let us know why, if you'd like.",
                    singleLine = false,
                    minLines = 2,
                )
                NoticeCard(
                    text = "No records are deleted. The appointment is simply marked Cancelled.",
                )
            },
        )
    }
}
