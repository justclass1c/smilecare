package com.class1c.smilecare.features.inventory.itemform.presentation

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusTone

/**
 * Create form for a consumable stock item. Validation is presented from the
 * state's error fields; [onSaved] fires once the repository accepted the record.
 */
@Composable
fun ItemFormScreen(
    state: ItemFormUIState,
    onEvent: (ItemFormEvent) -> Unit,
    onBackClick: () -> Unit = {},
    onSaved: () -> Unit = {},
) {
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved()
    }

    SmileScreen(
        title = "Add item",
        onBackClick = onBackClick,
    ) {
        Text(
            text = "Supplies and stock, kept ready for care.",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "Add an item so the team knows what is on the shelf.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "Fields marked * are required.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (state.invalidName != null || state.invalidQuantity != null || state.invalidPrice != null) {
            NoticeCard(
                text = "Please check the highlighted fields below.",
                title = "A few details need your attention",
                tone = StatusTone.DANGER,
            )
        }

        if (state.errorMessage != null) {
            NoticeCard(
                text = state.errorMessage,
                title = "Could not save the item",
                tone = StatusTone.DANGER,
            )
        }

        SectionTitle(title = "Item details")
        SmileTextField(
            label = "Name *",
            value = state.name,
            onValueChange = { onEvent(ItemFormEvent.UpdateName(it)) },
            onBlur = { onEvent(ItemFormEvent.ValidateInput(ItemFormField.NAME)) },
            placeholder = "e.g. Nitrile gloves",
            isError = state.invalidName != null,
            errorText = state.invalidName,
        )

        SmileTextField(
            label = "Quantity *",
            value = state.quantity,
            onValueChange = { onEvent(ItemFormEvent.UpdateQuantity(it)) },
            onBlur = { onEvent(ItemFormEvent.ValidateInput(ItemFormField.QUANTITY)) },
            placeholder = "0",
            isError = state.invalidQuantity != null,
            errorText = state.invalidQuantity,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        SmileTextField(
            label = "Unit price (RM) *",
            value = state.price,
            onValueChange = { onEvent(ItemFormEvent.UpdatePrice(it)) },
            onBlur = { onEvent(ItemFormEvent.ValidateInput(ItemFormField.PRICE)) },
            placeholder = "0.00",
            isError = state.invalidPrice != null,
            errorText = state.invalidPrice,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        )

        SmileCard {
            PrimaryButton(
                text = if (state.isSaving) "Saving..." else "Save item",
                onClick = { onEvent(ItemFormEvent.SaveItem) },
                enabled = !state.isSaving,
                leadingIcon = Icons.Filled.Check,
            )
            Text(
                text = "The new item is added to the inventory list.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
