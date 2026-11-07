package com.class1c.smilecare.domain.model

/**
 * Catalogue entry for a service the clinic offers.
 *
 * `price` is the current list price; invoices should copy it into Payment so
 * historic bills do not change when the catalogue price is updated later.
 */
data class Service(
    val serviceID: String,
    val serviceName: String,
    val description: String,
    val price: Double,
    val modifiedDate: String,
)
