package com.class1c.smilecare.data.remote.row

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceRow(
    @SerialName("service_id") val serviceID: String,
    @SerialName("service_name") val serviceName: String,
    val description: String,
    val price: Float,
    @SerialName("modified_date") val modifiedDate: String
)