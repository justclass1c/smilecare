package com.class1c.smilecare.features.inventory.equipmentform.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.class1c.smilecare.domain.model.Equipment
import com.class1c.smilecare.domain.repository.EquipmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EquipmentFormViewModel @Inject constructor(
    private val equipmentRepository: EquipmentRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EquipmentFormUIState())

    val uiState: StateFlow<EquipmentFormUIState> = _uiState.asStateFlow()

    fun onEvent(event: EquipmentFormEvent) = when (event) {
        is EquipmentFormEvent.UpdateName -> _uiState.update {
            it.copy(name = event.name, invalidName = null, errorMessage = null)
        }

        is EquipmentFormEvent.UpdateLocation -> _uiState.update {
            it.copy(location = event.location, invalidLocation = null, errorMessage = null)
        }

        is EquipmentFormEvent.ValidateInput -> _uiState.update {
            when (event.field) {
                EquipmentFormField.NAME -> it.copy(invalidName = validateName(it.name))
                EquipmentFormField.LOCATION -> it.copy(invalidLocation = validateLocation(it.location))
            }
        }

        EquipmentFormEvent.SaveEquipment -> saveEquipment()
    }

    private fun saveEquipment() {
        val state = _uiState.value
        val nameError = validateName(state.name)
        val locationError = validateLocation(state.location)

        if (nameError != null || locationError != null) {
            _uiState.update {
                it.copy(invalidName = nameError, invalidLocation = locationError)
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            runCatching {
                equipmentRepository.insertEquipment(
                    Equipment(
                        equipmentID = "",
                        equipmentName = state.name.trim(),
                        location = state.location,
                    )
                )
            }.onSuccess {
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = error.message ?: "Could not save the asset.",
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

    private fun validateLocation(location: String): String? {
        if (location.isBlank()) return "Please select a location."
        if (location !in EQUIPMENT_LOCATIONS) return "Choose a valid location."
        return null
    }
}
