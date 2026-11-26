package com.class1c.smilecare.data.remote.row

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmbedNurse(
    @SerialName("nurse_id") val nurseId: String,
)
