package com.class1c.smilecare.features.manage.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.class1c.smilecare.domain.repository.EquipmentRepository
import com.class1c.smilecare.domain.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageClinicViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val equipmentRepository: EquipmentRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageClinicUIState())

    val uiState: StateFlow<ManageClinicUIState> = _uiState.asStateFlow()

    /** Reloads the inventory and equipment counts from Supabase. */
    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching {
                itemRepository.listItems().size to equipmentRepository.listEquipment().size
            }.onSuccess { (inventoryCount, equipmentCount) ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        inventoryCount = inventoryCount,
                        equipmentCount = equipmentCount,
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Could not load clinic totals.",
                    )
                }
            }
        }
    }
}
