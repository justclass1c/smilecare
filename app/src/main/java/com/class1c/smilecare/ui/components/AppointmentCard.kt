package com.class1c.smilecare.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.Appointment
import com.class1c.smilecare.domain.model.AppointmentStatus

/** Dumb, reusable UI: it receives state and emits events, it never touches a ViewModel. */
@Composable
fun AppointmentCard(
    appointment: Appointment,
    onCancel: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = appointment.dentistName,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "${appointment.date} at ${appointment.time}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = appointment.status.name.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelLarge,
                    color = when (appointment.status) {
                        AppointmentStatus.UPCOMING -> MaterialTheme.colorScheme.primary
                        AppointmentStatus.COMPLETED -> MaterialTheme.colorScheme.onSurfaceVariant
                        AppointmentStatus.CANCELLED -> MaterialTheme.colorScheme.error
                    },
                )
                if (appointment.status == AppointmentStatus.UPCOMING) {
                    TextButton(onClick = { onCancel(appointment.id) }) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}
