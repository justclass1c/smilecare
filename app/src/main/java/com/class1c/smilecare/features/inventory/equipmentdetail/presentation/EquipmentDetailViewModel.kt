package com.class1c.smilecare.features.inventory.equipmentdetail.presentation

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
class EquipmentDetailViewModel @Inject constructor(
    private val equipmentRepository: EquipmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EquipmentDetailUIState())

    val uiState: StateFlow<EquipmentDetailUIState> = _uiState.asStateFlow()

    /** Loads the asset that matches [equipmentName] from the Equipment table. */
    fun load(equipmentName: String) {
        if (equipmentName.isBlank()) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(equipmentName = equipmentName, isLoading = true, errorMessage = null)
            }
            runCatching { equipmentRepository.listEquipment() }
                .onSuccess { items ->
                    val equipment = items.firstOrNull { it.equipmentName == equipmentName }
                    if (equipment == null) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "This asset no longer exists.",
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                equipmentName = equipment.equipmentName,
                                location = equipment.location,
                                status = equipment.status,
                            )
                        }
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Could not load this asset.",
                        )
                    }
                }
        }
    }

    fun onEvent(event: EquipmentDetailEvent) = when (event) {
        is EquipmentDetailEvent.SelectStatus -> _uiState.update {
            it.copy(status = event.status, errorMessage = null)
        }

        EquipmentDetailEvent.SaveStatus -> saveStatus()
        EquipmentDetailEvent.Reload -> load(_uiState.value.equipmentName)
    }

    private fun saveStatus() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            runCatching {
                equipmentRepository.updateEquipmentStatus(
                    equipmentName = state.equipmentName,
                    status = state.status,
                )
            }.onSuccess {
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = error.message ?: "Could not update the status.",
                    )
                }
            }
        }
    }
}
