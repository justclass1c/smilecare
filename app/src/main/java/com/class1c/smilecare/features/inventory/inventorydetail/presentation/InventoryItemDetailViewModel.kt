package com.class1c.smilecare.features.inventory.inventorydetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.class1c.smilecare.domain.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryItemDetailViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryItemDetailUIState())

    val uiState: StateFlow<InventoryItemDetailUIState> = _uiState.asStateFlow()

    /** Loads the item that matches [itemName] from the Inventory table. */
    fun load(itemName: String) {
        if (itemName.isBlank()) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(itemName = itemName, isLoading = true, errorMessage = null)
            }
            runCatching { itemRepository.listItems() }
                .onSuccess { items ->
                    val item = items.firstOrNull { it.itemName == itemName }
                    if (item == null) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "This item no longer exists.",
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                itemName = item.itemName,
                                amount = item.quantity.toString(),
                                price = item.price,
                            )
                        }
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Could not load this item.",
                        )
                    }
                }
        }
    }

    fun onEvent(event: InventoryItemDetailEvent) = when (event) {
        is InventoryItemDetailEvent.UpdateAmount -> _uiState.update {
            it.copy(amount = event.amount, invalidAmount = null, errorMessage = null)
        }

        is InventoryItemDetailEvent.ValidateInput -> _uiState.update {
            when (event.field) {
                InventoryItemDetailField.AMOUNT -> it.copy(
                    invalidAmount = validateAmount(it.amount),
                )
            }
        }

        InventoryItemDetailEvent.SaveItem -> saveAmount()
        InventoryItemDetailEvent.Reload -> load(_uiState.value.itemName)
        InventoryItemDetailEvent.RequestDelete -> _uiState.update {
            it.copy(showDeleteDialog = true)
        }

        InventoryItemDetailEvent.DismissDelete -> _uiState.update {
            it.copy(showDeleteDialog = false)
        }

        InventoryItemDetailEvent.ConfirmDelete -> deleteItem()
    }

    private fun saveAmount() {
        val state = _uiState.value
        val amountError = validateAmount(state.amount)

        if (amountError != null) {
            _uiState.update { it.copy(invalidAmount = amountError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            runCatching {
                itemRepository.updateItemAmount(
                    itemName = state.itemName,
                    quantity = state.amount.trim().toInt(),
                )
            }.onSuccess {
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = error.message ?: "Could not update the amount.",
                    )
                }
            }
        }
    }

    private fun deleteItem() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update {
                it.copy(isSaving = true, showDeleteDialog = false, errorMessage = null)
            }
            runCatching {
                itemRepository.deleteItem(state.itemName)
            }.onSuccess {
                _uiState.update { it.copy(isSaving = false, isDeleted = true) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = error.message ?: "Could not delete this item.",
                    )
                }
            }
        }
    }

    private fun validateAmount(amount: String): String? {
        if (amount.isBlank()) return "Amount cannot be empty."
        val value = amount.trim().toIntOrNull() ?: return "Amount must be a whole number."
        if (value < 0) return "Amount cannot be negative."
        if (value > 999_999) return "Amount cannot be more than 999999."
        return null
    }
}
