package com.class1c.smilecare.data.remote.row

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DentistRow (
    @SerialName("user_id") val userID: String,
    val specialty: String
)