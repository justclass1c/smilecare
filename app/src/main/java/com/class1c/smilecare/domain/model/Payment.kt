package com.class1c.smilecare.domain.model

data class Payment (
    val paymentID: String,
    val appointmentID: String, // foreign key, use reference
    val treatments: String, // String is temporary. to define treatment class for service catalogue
    val totalAmount: Double,
    val method: PaymentMethod,
    val date: String,
    val status: PaymentStatus
)

enum class PaymentMethod {
    CARD,
    CASH,
}

enum class PaymentStatus {
    PENDING,
    PAID,
    OVERDUE
}