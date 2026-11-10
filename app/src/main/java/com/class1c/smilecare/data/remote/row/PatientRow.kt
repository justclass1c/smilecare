package com.class1c.smilecare.data.remote.row

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatientRow(
    @SerialName("user_id") val userId: String,
    @SerialName("has_insurance") val hasInsurance: Boolean,
    val allergies: String = "",
    @SerialName("medical_notes") val medicalNotes: String = "",
)