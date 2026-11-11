package com.class1c.smilecare.data.remote.row

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NurseRow(
    @SerialName("user_id") val userID: String = "",
    @SerialName("nurse_id") val nurseID: String = "",
)