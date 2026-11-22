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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.ReminderNotificationCard
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTabs
import com.class1c.smilecare.ui.components.TextAction
import com.class1c.smilecare.ui.components.ToggleRow

/**
 * Reminder settings. Toggle and lead-time selection are hoisted so the screen
 * stays free of persistence logic.
 */
@Composable
fun RemindersScreen(
    remindersEnabled: Boolean = true,
    selectedLeadTimes: Set<String> = setOf("1 day before"),
    onRemindersChange: (Boolean) -> Unit = {},
    onLeadTimeToggle: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onPreviewClick: () -> Unit = {},
) {
    SmileScreen(
        title = "Reminders",
        onBackClick = onBackClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
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
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "A gentle nudge.",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "One less thing to remember.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        ToggleRow(
            label = "Appointment reminders",
            description = "Receive a notification before your visit.",
            checked = remindersEnabled,
            onCheckedChange = onRemindersChange,
        )

        SectionTitle(title = "Remind me")
        SmileTabs(
            options = leadTimeOptions,
            selectedIndex = leadTimeOptions.indexOfFirst { it in selectedLeadTimes }
                .coerceAtLeast(0),
            onSelect = { index -> onLeadTimeToggle(leadTimeOptions[index]) },
        )
        Text(
            text = "Choose one or more reminder times.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        SectionTitle(title = "Your notification preview")
        if (remindersEnabled && selectedLeadTimes.isNotEmpty()) {
            ReminderNotificationCard(
                message = "Reminder: Service name tomorrow at 00:00 AM with Dentist name",
            )
        } else {
            NoticeCard(
                text = "Reminders are paused. Turn them on and choose a lead time to preview your notification.",
            )
        }

        NoticeCard(
            text = "Only appointment details appear in reminders. You can change these settings anytime.",
        )
        TextAction(
            text = "Preview notification shade",
            onClick = onPreviewClick,
        )
    }
}

private val leadTimeOptions = listOf(
    "1 day before",
    "3 hours before",
    "1 hour before",
)
