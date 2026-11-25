package com.class1c.smilecare.features.appointments.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.ui.components.ReminderNotificationCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.TextAction

/**
 * Presentation-only mock of the device notification shade. It exists to
 * preview how the appointment reminder appears outside the app.
 */
@Composable
fun ReminderPreviewScreen(
    onBackClick: () -> Unit = {},
    onViewClick: () -> Unit = {},
) {
    SmileScreen(
        title = "Notification preview",
        onBackClick = onBackClick,
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "00:00",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Light,
                    )
                    Text(
                        text = "DAY, DD MONTH YYYY",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    QuickSetting(
                        icon = Icons.Filled.Notifications,
                        active = true,
                        modifier = Modifier.weight(1f),
                    )
                    QuickSetting(
                        icon = Icons.Filled.Lock,
                        active = true,
                        modifier = Modifier.weight(1f),
                    )
                    QuickSetting(
                        icon = Icons.Filled.Phone,
                        active = false,
                        modifier = Modifier.weight(1f),
                    )
                    QuickSetting(
                        icon = Icons.Filled.Settings,
                        active = false,
                        modifier = Modifier.weight(1f),
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Spa,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                        )
                    }
                }

                Text(
                    text = "NOTIFICATIONS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                )

                ReminderNotificationCard(
                    message = "Reminder: Service name tomorrow at 00:00 AM with Dentist name",
                    onActionClick = onViewClick,
                )

                Text(
                    text = "Your notifications are up to date.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        TextAction(text = "Back to reminders", onClick = onBackClick)
        Spacer(Modifier.size(8.dp))
    }
}

@Composable
private fun QuickSetting(
    icon: ImageVector,
    active: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.size(50.dp),
        shape = CircleShape,
        color = if (active) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surface
        },
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = if (active) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }
    }
}
