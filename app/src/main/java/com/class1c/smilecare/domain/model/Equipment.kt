package com.class1c.smilecare.domain.model

/** A physical asset owned by the clinic (e.g. an X-ray machine or dental chair). */
data class Equipment(
    val equipmentID: String,
    val equipmentName: String,
)
