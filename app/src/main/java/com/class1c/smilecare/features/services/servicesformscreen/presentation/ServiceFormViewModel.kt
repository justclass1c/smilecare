package com.class1c.smilecare.features.services.servicesformscreen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.class1c.smilecare.domain.model.Service
import com.class1c.smilecare.domain.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ServiceFormViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ServiceFormUIState())

    val uiState: StateFlow<ServiceFormUIState> = _uiState.asStateFlow()

    fun onEvent(event: ServiceFormEvent) = when(event) {
        ServiceFormEvent.ToggleActive -> _uiState.update { it.copy(isActive = !it.isActive) }
        is ServiceFormEvent.UpdateDescription -> _uiState.update { it.copy(description = event.description) }
        is ServiceFormEvent.UpdateDuration -> _uiState.update { it.copy(duration = event.duration) }
        is ServiceFormEvent.UpdateName -> _uiState.update { it.copy(name = event.name) }
        is ServiceFormEvent.UpdatePrice -> _uiState.update { it.copy(price = event.price) }
        is ServiceFormEvent.ValidateInput -> _uiState.update {
            when(event.field) {
                ServiceFormField.NAME -> it.copy(invalidName = validateServiceName(it.name))
                ServiceFormField.DESCRIPTION -> it.copy(invalidDescription = validateServiceDescription(it.description))
                ServiceFormField.PRICE -> it.copy(invalidPrice = validateServicePrice(it.price))
                ServiceFormField.DURATION -> it.copy(invalidDuration = validateServiceDuration(it.duration))
            }
        }
        ServiceFormEvent.SaveService -> {
            val service = _uiState.value
            val serviceNameErr = validateServiceName(service.name)
            val serviceDescErr = validateServiceDescription(service.description)
            val servicePriceErr = validateServicePrice(service.price)
            val serviceDurationErr = validateServiceDuration(service.duration)

            val allValid = serviceNameErr == null && serviceDescErr == null && servicePriceErr == null && serviceDurationErr == null
            if (!allValid) {
                _uiState.update {
                    it.copy(
                        invalidName = serviceNameErr,
                        invalidDescription = serviceDescErr,
                        invalidPrice = servicePriceErr,
                        invalidDuration = serviceDurationErr
                    )
                }
            } else {
                viewModelScope.launch {
                    serviceRepository.insertService(
                        Service(
                            serviceID = "",
                            serviceName = service.name,
                            description = service.description,
                            price = service.price.toFloat(),
                            modifiedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        )
                    )
                }
            }
        }
    }

    private fun validateServiceName(fullName: String): String? {
        if (fullName.isEmpty()) return "Name cannot be empty."
        else if (fullName.length > 50) return "Name cannot be longer than 50 characters."
        return null
    }


    private fun validateServiceDescription(description: String): String? {
        if (description.isEmpty()) return "Description cannot be empty."
        else if (description.length > 50) return "Description cannot be longer than 50 characters."
        return null
    }

    // TODO: entering more than one decimal point is not showing error
    private fun validateServicePrice(price: String): String? {
        if (price.isEmpty()) return "Price cannot be empty."
        else price.toFloatOrNull()?.let {
            if (it >= 10000.00) return "Price cannot be more than RM9999.99."
            else if (it < 10.00) return "Price cannot be less than RM10."
        }
        return null
    }

    // TODO: entering more than one decimal point is not showing error
    private fun validateServiceDuration(duration: String): String? {
        if (duration.isEmpty()) return "Price cannot be empty."
        else if (duration.toInt() > 1000) return "Duration cannot be longer than 1000 minutes."
        else if (duration.toInt() <= 0) return "Duration must be at least 1 minute."
        return null
    }
}