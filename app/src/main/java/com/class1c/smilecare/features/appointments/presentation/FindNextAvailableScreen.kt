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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.ui.components.HoursNote
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusPill
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.TextAction

/**
 * Earliest-slot finder. Results are controlled by [showResult] so the empty
 * and found variants share one destination.
 */
@Composable
fun FindNextAvailableScreen(
    showResult: Boolean = true,
    onBackClick: () -> Unit = {},
    onFindClick: () -> Unit = {},
    onBookClick: () -> Unit = {},
) {
    SmileScreen(
        title = "Find next available",
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
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "The sooner, the smilier.",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "We'll find the first opening for the care you need.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        SmileTextField(
            label = "Service",
            value = "",
            placeholder = "Service name",
        )
        SmileTextField(
            label = "Dentist",
            value = "",
            placeholder = "Any available dentist",
        )
        PrimaryButton(
            text = "Find earliest slot",
            onClick = onFindClick,
            leadingIcon = Icons.Filled.Search,
        )
        HoursNote()

        if (!showResult) {
            NoticeCard(
                text = "Choose a service and dentist, then search for the earliest opening.",
            )
            return@SmileScreen
        }

        NoticeCard(
            text = "Today's remaining slots are full. Here's your next opening.",
        )

        SectionTitle(title = "Earliest available")
        SmileCard {
            StatusPill(text = "Earliest available", tone = StatusTone.SUCCESS)
            Text(
                text = "DD/MM/YY · 00:00 AM",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Service name · RM 00.00",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "Dentist name · 0/3 booked · 1-hour appointment",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            PrimaryButton(text = "Book this slot", onClick = onBookClick)
        }

        SectionTitle(title = "A few more options")
        alternativeSlots.forEach { slot ->
            SmileCard(onClick = onBookClick) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = slot,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = "Dentist name · Available",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }

        TextAction(
            text = "Not sure what you need?",
            onClick = {},
        )
    }
}

private val alternativeSlots = listOf(
    "DD/MM/YY · 00:00 AM",
    "DD/MM/YY · 00:00 PM",
    "DD/MM/YY · 00:00 AM",
)
