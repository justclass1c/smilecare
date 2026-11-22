package com.class1c.smilecare.features.schedule.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
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
import com.class1c.smilecare.navigation.Route
import com.class1c.smilecare.ui.components.AppointmentSummaryCard
import com.class1c.smilecare.ui.components.HoursNote
import com.class1c.smilecare.ui.components.InfoLine
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.StatusPill
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.TextAction

/**
 * Dentist day view. The selected day is hoisted; appointments stay sample
 * presentation data until a ViewModel supplies them.
 */
@Composable
fun ScheduleScreen(
    role: UserRole = UserRole.DENTIST,
    selectedDay: Int = 4,
    onDaySelect: (Int) -> Unit = {},
    onAppointmentClick: (String) -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    SmileScreen(
        role = role,
        currentRoute = Route.Schedule,
        title = "My schedule",
        onNavigate = onNavigate,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "MONTH YYYY",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            StatusPill(text = "Today", tone = StatusTone.INFO)
        }

        WeekStrip(
            selectedDay = selectedDay,
            onDaySelect = onDaySelect,
        )

        SectionTitle(title = "Today's appointments")
        Text(
            text = "0 appointments",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HoursNote()

        scheduleAppointments.forEach { appointment ->
            TimelineRow(
                appointment = appointment,
                onClick = { onAppointmentClick(appointment.appointmentId) },
            )
        }

        NoticeCard(
            text = "Completed visits are sent to reception for invoicing.",
        )
    }
}

@Composable
private fun WeekStrip(
    selectedDay: Int,
    onDaySelect: (Int) -> Unit,
) {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        days.forEachIndexed { index, label ->
            val day = index + 2
            val selected = day == selectedDay
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onDaySelect(day) },
                shape = RoundedCornerShape(12.dp),
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surface
                },
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    },
                ),
                tonalElevation = 0.dp,
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                    Text(
                        text = day.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineRow(
    appointment: ScheduleAppointment,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Column(
            modifier = Modifier.width(52.dp).padding(top = 4.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = appointment.time,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = appointment.meridiem,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        SmileCard(modifier = Modifier.weight(1f), onClick = onClick) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = appointment.patient,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                StatusPill(text = appointment.status, tone = appointment.tone)
            }
            Text(
                text = appointment.service,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                InfoLine(
                    icon = Icons.Filled.CalendarMonth,
                    text = "Room ${appointment.room} · 1 hour",
                )
                if (appointment.current) {
                    TextAction(text = "View", onClick = onClick)
                }
            }
        }
    }
}

private data class ScheduleAppointment(
    val appointmentId: String,
    val time: String,
    val meridiem: String,
    val patient: String,
    val service: String,
    val status: String,
    val tone: StatusTone,
    val room: Int,
    val current: Boolean = false,
)

private val scheduleAppointments = listOf(
    ScheduleAppointment(
        appointmentId = "APT-0001",
        time = "00:00",
        meridiem = "AM",
        patient = "Patient name",
        service = "Service name",
        status = "Completed",
        tone = StatusTone.SUCCESS,
        room = 1,
    ),
    ScheduleAppointment(
        appointmentId = "APT-0002",
        time = "00:00",
        meridiem = "AM",
        patient = "Patient name",
        service = "Service name",
        status = "Scheduled",
        tone = StatusTone.INFO,
        room = 1,
        current = true,
    ),
    ScheduleAppointment(
        appointmentId = "APT-0003",
        time = "00:00",
        meridiem = "PM",
        patient = "Patient name",
        service = "Service name",
        status = "Scheduled",
        tone = StatusTone.INFO,
        room = 2,
    ),
    ScheduleAppointment(
        appointmentId = "APT-0004",
        time = "00:00",
        meridiem = "PM",
        patient = "Patient name",
        service = "Service name",
        status = "Scheduled",
        tone = StatusTone.INFO,
        room = 2,
    ),
)
