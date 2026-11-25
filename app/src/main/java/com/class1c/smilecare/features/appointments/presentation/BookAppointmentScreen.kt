package com.class1c.smilecare.features.appointments.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MedicalServices
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
import com.class1c.smilecare.ui.components.ChoiceCard
import com.class1c.smilecare.ui.components.DetailRow
import com.class1c.smilecare.ui.components.HoursNote
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.OutlineButton
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusPill
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.Stepper
import com.class1c.smilecare.ui.components.SuccessBanner

/**
 * Three-step booking flow. The caller controls the step, so the same screen
 * can be resumed, reviewed or reused for a reschedule later.
 */
@Composable
fun BookAppointmentScreen(
    role: UserRole = UserRole.PATIENT,
    step: Int = 1,
    showError: Boolean = false,
    showSuccess: Boolean = false,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    onViewAppointmentClick: () -> Unit = {},
    onSetReminderClick: () -> Unit = {},
) {
    val safeStep = step.coerceIn(1, 3)
    SmileScreen(
        title = "Book appointment",
        onBackClick = onBackClick,
    ) {
        Stepper(currentStep = safeStep)
        if (showSuccess) {
            SuccessBanner("Appointment booked successfully")
            Text(
                text = "Your next healthy smile is on the calendar.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            BookingSummaryStrip()
            PrimaryButton(
                text = "View appointment",
                onClick = onViewAppointmentClick,
            )
            OutlineButton(
                text = "Set reminder",
                onClick = onSetReminderClick,
            )
            return@SmileScreen
        }
        if (showError) {
            NoticeCard(
                text = "Please check the highlighted details below.",
                title = "We couldn't book this appointment",
                tone = StatusTone.DANGER,
            )
        }
        when (safeStep) {
            1 -> StepOne(role = role, onNextClick = onNextClick)
            2 -> StepTwo(onNextClick = onNextClick)
            else -> StepThree(onConfirmClick = onConfirmClick)
        }
    }
}

@Composable
private fun BookingSummaryStrip() {
    SmileCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Service name",
                    style = MaterialTheme.typography.titleSmall,
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
}

@Composable
private fun StepOne(role: UserRole, onNextClick: () -> Unit) {
    Text(
        text = "The right care for your smile.",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.SemiBold,
    )
    Text(
        text = "Choose a service and who you'd like to see.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    if (role == UserRole.NURSE) {
        SmileTextField(
            label = "Patient",
            value = "",
            placeholder = "Patient name",
        )
    }

    SectionTitle(title = "Choose a service *")
    val services = listOf(
        "Service name",
        "Service name",
        "Service name",
    )
    services.forEachIndexed { index, service ->
        ChoiceCard(
            selected = index == 0,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.MedicalServices,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Column {
                Text(
                    text = service,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "Estimated fee · RM 00.00",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }

    SectionTitle(title = "Choose a dentist *")
    DentistChoice(
        title = "Any available dentist",
        subtitle = "The earliest care, with any of our dentists",
        selected = true,
        isAny = true,
    )
    DentistChoice(
        title = "Dentist name",
        subtitle = "General Dentist",
        selected = false,
    )
    DentistChoice(
        title = "Dentist name",
        subtitle = "General Dentist",
        selected = false,
    )

    SmileTextField(
        label = "Reason / notes",
        value = "",
        placeholder = "Error text",
        singleLine = false,
        minLines = 3,
    )

    BookingFooter(
        label = "Next",
        onClick = onNextClick,
    )
}

@Composable
private fun DentistChoice(
    title: String,
    subtitle: String,
    selected: Boolean,
    isAny: Boolean = false,
) {
    ChoiceCard(selected = selected) {
        if (isAny) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Group,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            Avatar(initials = "DN", size = 40.dp)
        }
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun StepTwo(onNextClick: () -> Unit) {
    Text(
        text = "Make a little time for you.",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.SemiBold,
    )
    Text(
        text = "Choose a day and a one-hour appointment slot.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    HoursNote()

    SectionTitle(title = "Select a date *")
    SmileCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "MONTH YYYY",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "<",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = ">",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        CalendarGrid()
    }

    SectionTitle(title = "Available times *")
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            listOf("00:00 AM", "00:00 AM", "00:00 AM"),
            listOf("00:00 PM", "00:00 PM", "00:00 PM"),
        ).forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                row.forEachIndexed { columnIndex, time ->
                    TimeSlot(
                        text = time,
                        selected = rowIndex == 0 && columnIndex == 0,
                        enabled = !(rowIndex == 0 && columnIndex == 2),
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }

    BookingFooter(
        label = "Next",
        onClick = onNextClick,
    )
}

@Composable
private fun CalendarGrid() {
    val days = listOf("M", "T", "W", "T", "F", "S", "S")
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            days.forEach { day ->
                Box(
                    modifier = Modifier.size(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        (1..30).chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                week.forEach { day ->
                    val selected = day == 15
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = day.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (selected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        )
                    }
                }
                repeat(7 - week.size) {
                    Box(Modifier.size(32.dp))
                }
            }
        }
    }
}

@Composable
private fun TimeSlot(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.clickable(enabled = enabled) {},
        shape = RoundedCornerShape(10.dp),
        color = when {
            selected -> MaterialTheme.colorScheme.primary
            enabled -> MaterialTheme.colorScheme.surface
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            },
        ),
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            style = MaterialTheme.typography.labelMedium,
            color = when {
                selected -> MaterialTheme.colorScheme.onPrimary
                enabled -> MaterialTheme.colorScheme.onSurface
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
    }
}

@Composable
private fun StepThree(onConfirmClick: () -> Unit) {
    Text(
        text = "One last look. One step closer to a smile.",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.SemiBold,
    )
    Text(
        text = "Make sure everything looks right before booking.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    SectionTitle(title = "Your appointment")
    SmileCard {
        DetailRow(label = "Service", value = "Service name")
        DetailRow(label = "Dentist", value = "Dentist name")
        DetailRow(label = "Date", value = "DD/MM/YY")
        DetailRow(label = "Time", value = "00:00 AM")
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
        text = "Please arrive 10 minutes early and bring your NRIC. You'll receive a confirmation after booking.",
    )

    BookingFooter(
        label = "Confirm booking",
        onClick = onConfirmClick,
    )
}

@Composable
private fun BookingFooter(
    label: String,
    onClick: () -> Unit,
) {
    SmileCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Price estimate",
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
        PrimaryButton(text = label, onClick = onClick)
        Text(
            text = "Final fees depend on the treatment provided.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
