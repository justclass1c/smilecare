package com.class1c.smilecare.data.remote.row

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JoinedUserNurseRow(
    @SerialName("user_id") val userId: String = "",
    val name: String,
    val email: String,
    val password: String,
    val nric: String,
    @SerialName("phone_no") val phoneNo: String,
    @SerialName("date_of_birth") val dateOfBirth: String,
    val gender: String,
    val role: String,

    val nurses: EmbedNurse? = null
)
