package com.class1c.smilecare.features.inventory.equipmentform.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusTone

/**
 * Create form for a clinic asset. Validation is presented from the state's error
 * fields; [onSaved] fires once the repository accepted the new record.
 */
@Composable
fun EquipmentFormScreen(
    state: EquipmentFormUIState,
    onEvent: (EquipmentFormEvent) -> Unit,
    onBackClick: () -> Unit = {},
    onSaved: () -> Unit = {},
) {
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved()
    }

    SmileScreen(
        title = "Add equipment",
        onBackClick = onBackClick,
    ) {
        Text(
            text = "Keep every chair, drill and X-ray ready for care.",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "Register a new asset so maintenance stays on schedule.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "Fields marked * are required.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (state.invalidName != null || state.invalidLocation != null) {
            NoticeCard(
                text = "Please check the highlighted fields below.",
                title = "A few details need your attention",
                tone = StatusTone.DANGER,
            )
        }

        if (state.errorMessage != null) {
            NoticeCard(
                text = state.errorMessage,
                title = "Could not save the asset",
                tone = StatusTone.DANGER,
            )
        }

        SectionTitle(title = "Equipment details")
        SmileTextField(
            label = "Name *",
            value = state.name,
            onValueChange = { onEvent(EquipmentFormEvent.UpdateName(it)) },
            onBlur = { onEvent(EquipmentFormEvent.ValidateInput(EquipmentFormField.NAME)) },
            placeholder = "e.g. Dental chair",
            isError = state.invalidName != null,
            errorText = state.invalidName,
        )

        LocationDropdown(
            selected = state.location,
            isError = state.invalidLocation != null,
            errorText = state.invalidLocation,
            onSelect = { onEvent(EquipmentFormEvent.UpdateLocation(it)) },
        )

        SmileCard {
            PrimaryButton(
                text = if (state.isSaving) "Saving..." else "Save equipment",
                onClick = { onEvent(EquipmentFormEvent.SaveEquipment) },
                enabled = !state.isSaving,
                leadingIcon = Icons.Filled.Check,
            )
            Text(
                text = "The new asset is added to the equipment register.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationDropdown(
    selected: String,
    isError: Boolean,
    errorText: String?,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            OutlinedTextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                label = { Text("Location *") },
                placeholder = { Text("Select a treatment room") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                isError = isError,
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    errorContainerColor = MaterialTheme.colorScheme.surface,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                EQUIPMENT_LOCATIONS.forEach { location ->
                    DropdownMenuItem(
                        text = { Text(location) },
                        onClick = {
                            onSelect(location)
                            expanded = false
                        },
                    )
                }
            }
        }
        if (errorText != null) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
