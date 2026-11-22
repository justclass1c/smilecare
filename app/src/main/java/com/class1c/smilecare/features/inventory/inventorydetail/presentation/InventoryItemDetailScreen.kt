package com.class1c.smilecare.features.inventory.inventorydetail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.ui.components.DetailRow
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.OutlineButton
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileAlertDialog
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.TextAction

/**
 * Inventory item detail. Loads the row from Supabase, lets the user change the
 * stock amount or delete the item, then reports back with [onFinished].
 */
@Composable
fun InventoryItemDetailScreen(
    itemName: String,
    state: InventoryItemDetailUIState,
    onEvent: (InventoryItemDetailEvent) -> Unit,
    onLoad: (String) -> Unit,
    onBackClick: () -> Unit = {},
    onFinished: () -> Unit = {},
) {
    LaunchedEffect(itemName) {
        onLoad(itemName)
    }
    LaunchedEffect(state.isSaved, state.isDeleted) {
        if (state.isSaved || state.isDeleted) onFinished()
    }

    SmileScreen(
        title = "Item details",
        onBackClick = onBackClick,
    ) {
        Text(
            text = "Update stock or remove this item.",
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
                    text = "Loading item...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else if (state.itemName.isEmpty() && state.errorMessage != null) {
            NoticeCard(
                text = state.errorMessage,
                title = "Could not load this item",
                tone = StatusTone.DANGER,
            )
            TextAction(
                text = "Try again",
                onClick = { onEvent(InventoryItemDetailEvent.Reload) },
            )
        } else {
            if (state.errorMessage != null) {
                NoticeCard(
                    text = state.errorMessage,
                    title = "Something went wrong",
                    tone = StatusTone.DANGER,
                )
            }

            SectionTitle(title = "Item")
            SmileCard {
                DetailRow(label = "Name", value = state.itemName)
                DetailRow(label = "Unit price", value = "RM %.2f".format(state.price))
            }

            SectionTitle(title = "Stock")
            SmileTextField(
                label = "Amount *",
                value = state.amount,
                onValueChange = { onEvent(InventoryItemDetailEvent.UpdateAmount(it)) },
                onBlur = {
                    onEvent(
                        InventoryItemDetailEvent.ValidateInput(InventoryItemDetailField.AMOUNT)
                    )
                },
                placeholder = "0",
                isError = state.invalidAmount != null,
                errorText = state.invalidAmount,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            SmileCard {
                PrimaryButton(
                    text = if (state.isSaving) "Saving..." else "Save amount",
                    onClick = { onEvent(InventoryItemDetailEvent.SaveItem) },
                    enabled = !state.isSaving,
                    leadingIcon = Icons.Filled.Check,
                )
                Text(
                    text = "Amounts below 50 are flagged as low stock.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            OutlineButton(
                text = "Delete item",
                onClick = { onEvent(InventoryItemDetailEvent.RequestDelete) },
                leadingIcon = Icons.Filled.Delete,
            )
        }
    }

    if (state.showDeleteDialog) {
        SmileAlertDialog(
            title = "Delete this item?",
            message = "\"${state.itemName}\" will be removed from the inventory. This cannot be undone.",
            confirmLabel = "Delete",
            onConfirmClick = { onEvent(InventoryItemDetailEvent.ConfirmDelete) },
            dismissLabel = "Cancel",
            onDismissClick = { onEvent(InventoryItemDetailEvent.DismissDelete) },
            tone = StatusTone.DANGER,
        )
    }
}
