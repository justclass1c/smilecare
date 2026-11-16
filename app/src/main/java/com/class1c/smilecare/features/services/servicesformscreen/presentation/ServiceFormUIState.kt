package com.class1c.smilecare.features.services.servicesformscreen.presentation

data class ServiceFormUIState(
    val name: String = "",
    val description: String = "",
    val price: String = "0.00",
    val duration: String = "0",
    val isActive: Boolean = false,

    // errors:
    val invalidName: String? = null,
    val invalidDescription: String? = null,
    val invalidPrice: String? = null,
    val invalidDuration: String? = null,
)