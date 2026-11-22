package com.class1c.smilecare.features.inventory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.class1c.smilecare.domain.repository.EquipmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EquipmentViewModel @Inject constructor(
    private val equipmentRepository: EquipmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EquipmentUIState())

    val uiState: StateFlow<EquipmentUIState> = _uiState.asStateFlow()

    /** Reloads the register from Supabase. Safe to call whenever the screen is shown. */
    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { equipmentRepository.listEquipment() }
                .onSuccess { items ->
                    _uiState.update { it.copy(items = items, isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Could not load equipment.",
                        )
                    }
                }
        }
    }
}
