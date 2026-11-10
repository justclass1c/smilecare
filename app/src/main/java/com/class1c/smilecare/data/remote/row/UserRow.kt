package com.class1c.smilecare.data.remote.row

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// this is only used for inserting a row of data as a class into Supabase. prevents half-inserted rows
@Serializable
data class UserRow(
    // if variable names are different than the attribute names in supabase, use @SerialName
    // e.g. @SerialName(attribute name in supabase) val variableName: dataType
    @SerialName("user_id") val userId: String = "",
    val name: String,
    val email: String,
    val password: String,
    val nric: String,
    @SerialName("phone_no") val phoneNo: String,
    @SerialName("date_of_birth") val dateOfBirth: String,
    val gender: String,
    val role: String,
)