package com.class1c.smilecare.features.services.servicesformscreen.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.TextAction
import com.class1c.smilecare.ui.components.ToggleRow

/**
 * Reception service form shared by add and edit. Validation is presented from
 * the [showError] flag; all fields are placeholders.
 */
@Composable
fun ServiceFormScreen(
    isEditing: Boolean = false,
    showError: Boolean = false,
    onEvent: (ServiceFormEvent) -> Unit,
    state: ServiceFormUIState,
    onSaveClick: () -> Unit = {},
    onActiveChange: (Boolean) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    SmileScreen(
        title = if (isEditing) "Edit service" else "Add service",
        onBackClick = onBackClick,
    ) {
        Text(
            text = if (isEditing) {
                "A little update. Better care."
            } else {
                "Make room for better care."
            },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = if (isEditing) {
                "Update this service without changing existing invoices."
            } else {
                "Add a service and the details your team needs."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "Fields marked * are required.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (showError) {
            NoticeCard(
                text = "Please check the highlighted fields below.",
                title = "A few details need your attention",
                tone = StatusTone.DANGER,
            )
        }

        SectionTitle(title = "Service details")
        SmileTextField(
            label = "Name *",
            value = state.name,
            onValueChange = { onEvent(ServiceFormEvent.UpdateName(it)) },
            onBlur = { onEvent(ServiceFormEvent.ValidateInput(ServiceFormField.NAME)) },
            placeholder = "Service name",
            isError = state.invalidName != null,
            errorText = state.invalidName
        )

        SmileTextField(
            label = "Description *",
            value = state.description,
            onValueChange = { onEvent(ServiceFormEvent.UpdateDescription(it)) },
            onBlur = { onEvent(ServiceFormEvent.ValidateInput(ServiceFormField.DESCRIPTION)) },
            placeholder = "A short, patient-friendly explanation",
            isError = state.invalidDescription != null,
            errorText = state.invalidDescription,
            singleLine = false,
        )

        SmileTextField(
            label = "Price (RM) *",
            value = state.price,
            onValueChange = { onEvent(ServiceFormEvent.UpdatePrice(it)) },
            onBlur = { onEvent(ServiceFormEvent.ValidateInput(ServiceFormField.PRICE)) },
            isError = state.invalidPrice != null,
            errorText = state.invalidPrice,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        SmileTextField(
            label = "Duration (minutes) *",
            value = state.duration,
            onValueChange = { onEvent(ServiceFormEvent.UpdateDuration(it)) },
            onBlur = { onEvent(ServiceFormEvent.ValidateInput(ServiceFormField.DURATION)) },
            isError = state.invalidDuration != null,
            errorText = state.invalidDuration,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        ToggleRow(
            label = "Active?",
            description = "Active services are visible to patients and dentists.",
            checked = state.isActive,
            onCheckedChange = { onEvent(ServiceFormEvent.ToggleActive) },
        )

        SmileCard {
            PrimaryButton(
                text = "Save service",
                onClick = { onEvent(ServiceFormEvent.SaveService) },
                leadingIcon = Icons.Filled.Check,
            )
            Text(
                text = "Saved changes apply to future bookings. Invoice history stays unchanged.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
