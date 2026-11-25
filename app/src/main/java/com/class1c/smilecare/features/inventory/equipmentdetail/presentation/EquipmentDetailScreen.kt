package com.class1c.smilecare.features.inventory.equipmentdetail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.EQUIPMENT_STATUSES
import com.class1c.smilecare.ui.components.ChoiceCard
import com.class1c.smilecare.ui.components.DetailRow
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.TextAction

/**
 * Equipment detail. Loads the asset from Supabase and lets the user switch its
 * status between the four supported states.
 */
@Composable
fun EquipmentDetailScreen(
    equipmentName: String,
    state: EquipmentDetailUIState,
    onEvent: (EquipmentDetailEvent) -> Unit,
    onLoad: (String) -> Unit,
    onBackClick: () -> Unit = {},
    onFinished: () -> Unit = {},
) {
    LaunchedEffect(equipmentName) {
        onLoad(equipmentName)
    }
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onFinished()
    }

    SmileScreen(
        title = "Asset details",
        onBackClick = onBackClick,
    ) {
        Text(
            text = "Choose the status that matches this asset.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (state.isLoading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Loading asset...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else if (state.equipmentName.isEmpty() && state.errorMessage != null) {
            NoticeCard(
                text = state.errorMessage,
                title = "Could not load this asset",
                tone = StatusTone.DANGER,
            )
            TextAction(
                text = "Try again",
                onClick = { onEvent(EquipmentDetailEvent.Reload) },
            )
        } else {
            if (state.errorMessage != null) {
                NoticeCard(
                    text = state.errorMessage,
                    title = "Something went wrong",
                    tone = StatusTone.DANGER,
                )
            }

            SectionTitle(title = "Asset")
            SmileCard {
                DetailRow(label = "Name", value = state.equipmentName)
                DetailRow(
                    label = "Location",
                    value = state.location.ifBlank { "No location set" },
                )
            }

            SectionTitle(title = "Status")
            EQUIPMENT_STATUSES.forEach { option ->
                ChoiceCard(
                    selected = state.status == option,
                    onClick = { onEvent(EquipmentDetailEvent.SelectStatus(option)) },
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            SmileCard {
                PrimaryButton(
                    text = if (state.isSaving) "Saving..." else "Save status",
                    onClick = { onEvent(EquipmentDetailEvent.SaveStatus) },
                    enabled = !state.isSaving,
                    leadingIcon = Icons.Filled.Check,
                )
            }
        }
    }
}
