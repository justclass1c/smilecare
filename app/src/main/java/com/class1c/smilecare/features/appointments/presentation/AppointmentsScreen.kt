package com.class1c.smilecare.features.appointments.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.ExtendedFloatingActionButton
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
import com.class1c.smilecare.navigation.Route
import com.class1c.smilecare.ui.components.AppointmentSummaryCard
import com.class1c.smilecare.ui.components.HoursNote
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTabs
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.TextAction

/**
 * Appointment list. The tab index is hoisted so the screen stays stateless;
 * the same destination renders the patient, dentist or reception view.
 */
@Composable
fun AppointmentsScreen(
    role: UserRole,
    selectedTab: Int = 0,
    onTabChange: (Int) -> Unit = {},
    onAppointmentClick: (String) -> Unit = {},
    onBookClick: () -> Unit = {},
    onRemindersClick: () -> Unit = {},
    onFindNextClick: () -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    val title = when (role) {
        UserRole.PATIENT -> "My appointments"
        UserRole.DENTIST -> "My schedule"
        else -> "All appointments"
    }
    SmileScreen(
        role = role,
        currentRoute = Route.Appointments,
        title = title,
        onNavigate = onNavigate,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onBookClick,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                    )
                },
                text = { Text("Book") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        },
    ) {
        Text(
            text = when (role) {
                UserRole.PATIENT -> "A little planning. A healthier smile."
                UserRole.DENTIST -> "Your clinic day, in one place."
                else -> "Every visit, every patient."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Appointment reminders",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
            )
            TextAction(
                text = "Manage",
                onClick = onRemindersClick,
                icon = Icons.Filled.Notifications,
            )
        }

        SmileTabs(
            options = listOf("Upcoming", "Past", "Cancelled"),
            selectedIndex = selectedTab,
            onSelect = onTabChange,
        )

        val visible = when (selectedTab) {
            1 -> pastAppointments
            2 -> cancelledAppointments
            else -> upcomingAppointments
        }
        SectionTitle(
            title = when (role) {
                UserRole.PATIENT -> when (selectedTab) {
                    1 -> "Your past visits"
                    2 -> "Cancelled appointments"
                    else -> "Your next visits"
                }

                UserRole.DENTIST -> "Today's schedule"
                else -> "All visits"
            },
        )
        Text(
            text = "${visible.size} appointments",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        visible.forEach { appointment ->
            AppointmentSummaryCard(
                appointmentId = appointment.appointmentId,
                status = appointment.status,
                statusTone = appointment.statusTone,
                title = if (role == UserRole.PATIENT) {
                    appointment.service
                } else {
                    appointment.patient
                },
                subtitle = if (role == UserRole.PATIENT) null else appointment.service,
                dentistName = appointment.dentist,
                date = appointment.date,
                time = appointment.time,
                onClick = { onAppointmentClick(appointment.appointmentId) },
            )
        }

        if (visible.isEmpty()) {
            NoticeCard(
                text = "No appointments to show. Your next healthy smile can start here.",
            )
        }

        if (selectedTab == 2) {
            NoticeCard(
                text = "Cancelled appointments stay in your history. No records are deleted.",
            )
        }

        if (role == UserRole.PATIENT) {
            SmileCard(onClick = onFindNextClick) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Spa,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "Need a sooner visit?",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "Let us find your earliest available slot.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        HoursNote()
        Spacer(Modifier.height(72.dp))
    }
}

private data class AppointmentPreview(
    val appointmentId: String,
    val service: String,
    val dentist: String,
    val patient: String,
    val date: String,
    val time: String,
    val status: String,
    val statusTone: StatusTone,
)

private val upcomingAppointments = listOf(
    AppointmentPreview(
        appointmentId = "APT-0001",
        service = "Service name",
        dentist = "Dentist name",
        patient = "Patient name",
        date = "DD/MM/YY",
        time = "00:00 AM",
        status = "Scheduled",
        statusTone = StatusTone.INFO,
    ),
    AppointmentPreview(
        appointmentId = "APT-0002",
        service = "Service name",
        dentist = "Dentist name",
        patient = "Patient name",
        date = "DD/MM/YY",
        time = "00:00 PM",
        status = "Scheduled",
        statusTone = StatusTone.INFO,
    ),
)

private val pastAppointments = listOf(
    AppointmentPreview(
        appointmentId = "APT-0003",
        service = "Service name",
        dentist = "Dentist name",
        patient = "Patient name",
        date = "DD/MM/YY",
        time = "00:00 AM",
        status = "Completed",
        statusTone = StatusTone.SUCCESS,
    ),
)

private val cancelledAppointments = listOf(
    AppointmentPreview(
        appointmentId = "APT-0004",
        service = "Service name",
        dentist = "Dentist name",
        patient = "Patient name",
        date = "DD/MM/YY",
        time = "00:00 PM",
        status = "Cancelled",
        statusTone = StatusTone.NEUTRAL,
    ),
)
