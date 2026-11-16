package com.class1c.smilecare.features.services.servicesformscreen.presentation

enum class ServiceFormField {
    NAME, DESCRIPTION, PRICE, DURATION
}

sealed interface ServiceFormEvent {
    data class UpdateName(val name: String): ServiceFormEvent
    data class UpdateDescription(val description: String): ServiceFormEvent
    data class UpdatePrice(val price: String): ServiceFormEvent
    data class UpdateDuration(val duration: String): ServiceFormEvent
    data class ValidateInput(val field: ServiceFormField): ServiceFormEvent
    data object ToggleActive: ServiceFormEvent
    data object SaveService: ServiceFormEvent
}