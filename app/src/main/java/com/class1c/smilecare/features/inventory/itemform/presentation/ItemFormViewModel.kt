package com.class1c.smilecare.features.inventory.itemform.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.class1c.smilecare.domain.model.Item
import com.class1c.smilecare.domain.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemFormViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ItemFormUIState())

    val uiState: StateFlow<ItemFormUIState> = _uiState.asStateFlow()

    fun onEvent(event: ItemFormEvent) = when (event) {
        is ItemFormEvent.UpdateName -> _uiState.update {
            it.copy(name = event.name, invalidName = null, errorMessage = null)
        }

        is ItemFormEvent.UpdateQuantity -> _uiState.update {
            it.copy(quantity = event.quantity, invalidQuantity = null, errorMessage = null)
        }

        is ItemFormEvent.UpdatePrice -> _uiState.update {
            it.copy(price = event.price, invalidPrice = null, errorMessage = null)
        }

        is ItemFormEvent.ValidateInput -> _uiState.update {
            when (event.field) {
                ItemFormField.NAME -> it.copy(invalidName = validateName(it.name))
                ItemFormField.QUANTITY -> it.copy(invalidQuantity = validateQuantity(it.quantity))
                ItemFormField.PRICE -> it.copy(invalidPrice = validatePrice(it.price))
            }
        }

        ItemFormEvent.SaveItem -> saveItem()
    }

    private fun saveItem() {
        val state = _uiState.value
        val nameError = validateName(state.name)
        val quantityError = validateQuantity(state.quantity)
        val priceError = validatePrice(state.price)

        if (nameError != null || quantityError != null || priceError != null) {
            _uiState.update {
                it.copy(
                    invalidName = nameError,
                    invalidQuantity = quantityError,
                    invalidPrice = priceError,
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            runCatching {
                itemRepository.insertItem(
                    Item(
                        itemID = "",
                        itemName = state.name.trim(),
                        quantity = state.quantity.trim().toInt(),
                        price = state.price.trim().toFloat(),
                    )
                )
            }.onSuccess {
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = error.message ?: "Could not save the item. Please try again.",
                    )
                }
            }
        }
    }

    private fun validateName(name: String): String? {
        if (name.isBlank()) return "Name cannot be empty."
        if (name.trim().length > 50) return "Name cannot be longer than 50 characters."
        return null
    }

    private fun validateQuantity(quantity: String): String? {
        if (quantity.isBlank()) return "Quantity cannot be empty."
        val value = quantity.trim().toIntOrNull() ?: return "Quantity must be a whole number."
        if (value < 0) return "Quantity cannot be negative."
        if (value > 999_999) return "Quantity cannot be more than 999999."
        return null
    }

    private fun validatePrice(price: String): String? {
        if (price.isBlank()) return "Price cannot be empty."
        val value = price.trim().toFloatOrNull() ?: return "Price must be a number."
        if (value < 0f) return "Price cannot be negative."
        if (value > 99_999.99f) return "Price cannot be more than RM99999.99."
        return null
    }
}
