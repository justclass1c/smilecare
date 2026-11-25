package com.class1c.smilecare.features.home.presentation

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.class1c.smilecare.ui.components.MetricCard
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.QuickActionCard
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.StatusTone

/**
 * Role-aware dashboard. One destination, three presentations:
 * patient, dentist and reception.
 */
@Composable
fun HomeScreen(
    role: UserRole,
    onNotificationsClick: () -> Unit = {},
    onBookClick: () -> Unit = {},
    onAppointmentsClick: () -> Unit = {},
    onPrimaryQuickActionClick: () -> Unit = {},
    onSecondaryQuickActionClick: () -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    SmileScreen(
        role = role,
        currentRoute = Route.Home,
        onNavigate = onNavigate,
    ) {
        HomeGreeting(
            role = role,
            onNotificationsClick = onNotificationsClick,
        )
        CareBanner(
            role = role,
            onPrimaryActionClick = if (role == UserRole.PATIENT) onBookClick else onAppointmentsClick,
        )
        if (role != UserRole.PATIENT) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MetricCard(
                    label = "Today's appointments",
                    value = "00",
                    hint = "Error text",
                    modifier = Modifier.weight(1f),
                )
                MetricCard(
                    label = if (role == UserRole.DENTIST) "Patients treated" else "Collected today",
                    value = if (role == UserRole.DENTIST) "00" else "RM 0.00",
                    hint = "Error text",
                    modifier = Modifier.weight(1f),
                )
            }
        }
        SectionTitle(
            title = if (role == UserRole.PATIENT) "Upcoming appointment" else "Up next today",
            actionLabel = "View all",
            onActionClick = onAppointmentsClick,
        )
        UpcomingAppointmentCard(
            role = role,
            onClick = onAppointmentsClick,
        )
        SectionTitle(
            title = if (role == UserRole.PATIENT) "How can we help?" else "Quick actions",
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (role == UserRole.PATIENT) {
                QuickActionCard(
                    icon = Icons.Filled.CalendarMonth,
                    title = "Book a visit",
                    subtitle = "Find your time",
                    onClick = onPrimaryQuickActionClick,
                    modifier = Modifier.weight(1f),
                )
                QuickActionCard(
                    icon = Icons.Filled.MedicalServices,
                    title = "Our services",
                    subtitle = "Care for every smile",
                    onClick = onSecondaryQuickActionClick,
                    modifier = Modifier.weight(1f),
                )
            } else {
                QuickActionCard(
                    icon = Icons.Filled.CalendarMonth,
                    title = if (role == UserRole.DENTIST) "My schedule" else "Appointments",
                    subtitle = if (role == UserRole.DENTIST) "Today's visits" else "All visits",
                    onClick = onPrimaryQuickActionClick,
                    modifier = Modifier.weight(1f),
                )
                QuickActionCard(
                    icon = Icons.Filled.MedicalServices,
                    title = if (role == UserRole.DENTIST) "Service catalog" else "Manage services",
                    subtitle = "Services & pricing",
                    onClick = onSecondaryQuickActionClick,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        if (role == UserRole.PATIENT) {
            NoticeCard(
                text = "Floss once a day. Your future smile will thank you.",
                title = "A little smile advice",
                icon = Icons.Filled.Spa,
            )
        }
    }
}

@Composable
private fun HomeGreeting(
    role: UserRole,
    onNotificationsClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "DAY, DD MONTH",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "Hello, Name",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = ".",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Text(
                text = if (role == UserRole.PATIENT) {
                    "Let's keep your smile healthy."
                } else {
                    "A great day to make smiles happen."
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        ) {
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun CareBanner(
    role: UserRole,
    onPrimaryActionClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "A LITTLE CARE GOES A LONG WAY",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = if (role == UserRole.PATIENT) {
                        "Your smile, our priority."
                    } else {
                        "Better care. Brighter smiles."
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = if (role == UserRole.PATIENT) {
                        "Your next healthy chapter starts here."
                    } else {
                        "Here's your clinic at a glance."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Row(
                    modifier = Modifier.clickable(onClick = onPrimaryActionClick),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = if (role == UserRole.PATIENT) {
                            "Book an appointment"
                        } else {
                            "View today's schedule"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(78.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.MedicalServices,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Composable
private fun UpcomingAppointmentCard(
    role: UserRole,
    onClick: () -> Unit,
) {
    AppointmentSummaryCard(
        appointmentId = "APT-0000",
        status = "Scheduled",
        statusTone = StatusTone.INFO,
        title = if (role == UserRole.PATIENT) "Service name" else "Patient name",
        subtitle = if (role == UserRole.PATIENT) null else "Service name",
        dentistName = "Dentist name",
        date = "DD/MM/YY",
        time = "00:00 AM",
        onClick = onClick,
    )
}
