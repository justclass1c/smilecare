package com.class1c.smilecare.features.appointments.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.CalendarMonth
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
import com.class1c.smilecare.ui.components.Avatar
import com.class1c.smilecare.ui.components.DetailRow
import com.class1c.smilecare.ui.components.HoursNote
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.OutlineButton
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileDivider
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.StatusPill
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.TextAction

/**
 * Appointment detail. Actions are chosen from the role:
 * patient can reschedule or cancel, dentist can complete, reception can edit,
 * cancel or issue an invoice.
 */
@Composable
fun AppointmentDetailScreen(
    role: UserRole,
    appointmentId: String = "APT-0000",
    isCancelled: Boolean = false,
    isCompleted: Boolean = false,
    onBackClick: () -> Unit = {},
    onRescheduleClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onCompleteClick: () -> Unit = {},
    onIssueInvoiceClick: () -> Unit = {},
) {
    val status = when {
        isCancelled -> "Cancelled"
        isCompleted -> "Completed"
        else -> "Scheduled"
    }
    val statusTone = when {
        isCancelled -> StatusTone.NEUTRAL
        isCompleted -> StatusTone.SUCCESS
        else -> StatusTone.INFO
    }
    SmileScreen(
        title = "Appointment details",
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
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(28.dp),
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = appointmentId,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "Service name",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    StatusPill(text = status, tone = statusTone)
                }
            }
        }

        if (role != UserRole.PATIENT) {
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
                            text = "Age · 000-000 0000",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "NRIC · 000000-00-0000",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        SectionTitle(title = "Appointment details")
        SmileCard {
            DetailRow(label = "Service", value = "Service name")
            SmileDivider()
            DetailRow(label = "Dentist", value = "Dentist name")
            SmileDivider()
            DetailRow(label = "Date", value = "DD/MM/YY")
            SmileDivider()
            DetailRow(label = "Time", value = "00:00 AM – 00:00 PM")
            SmileDivider()
            DetailRow(label = "Duration", value = "00 min")
            SmileDivider()
            DetailRow(label = "Price estimate", value = "RM 00.00")
        }

        SectionTitle(title = "Reason / notes")
        SmileCard {
            Text(
                text = "Error text",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        NoticeCard(
            text = "Please arrive 10 minutes early and bring your NRIC.",
        )

        if (isCancelled) {
            NoticeCard(
                text = "This appointment is Cancelled. The record and its history have been kept.",
            )
        } else {
            when (role) {
                UserRole.PATIENT -> {
                    if (isCompleted) {
                        NoticeCard(
                            text = "Treatment completed. Your visit is in your history.",
                            tone = StatusTone.SUCCESS,
                        )
                    } else {
                        OutlineButton(
                            text = "Reschedule",
                            onClick = onRescheduleClick,
                        )
                        TextAction(
                            text = "Cancel appointment",
                            onClick = onCancelClick,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }

                UserRole.DENTIST -> {
                    if (isCompleted) {
                        NoticeCard(
                            text = "Treatment completed. Reception will prepare the invoice.",
                            tone = StatusTone.SUCCESS,
                        )
                    } else {
                        PrimaryButton(
                            text = "Complete appointment",
                            onClick = onCompleteClick,
                        )
                    }
                }

                else -> {
                    OutlineButton(
                        text = "Edit appointment",
                        onClick = onRescheduleClick,
                    )
                    if (isCompleted) {
                        PrimaryButton(
                            text = "Issue invoice",
                            onClick = onIssueInvoiceClick,
                            leadingIcon = Icons.AutoMirrored.Filled.ReceiptLong,
                        )
                    }
                    TextAction(
                        text = "Cancel appointment",
                        onClick = onCancelClick,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }

        HoursNote()
    }
}
