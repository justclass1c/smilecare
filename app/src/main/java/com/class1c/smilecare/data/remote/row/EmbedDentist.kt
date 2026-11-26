package com.class1c.smilecare.data.remote.row

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmbedDentist(
    @SerialName("dentist_id") val dentistId: String,
    val specialty: String
)
