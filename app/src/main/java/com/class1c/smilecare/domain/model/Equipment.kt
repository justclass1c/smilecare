package com.class1c.smilecare.domain.model

/** A physical asset owned by the clinic (e.g. an X-ray machine or dental chair). */
data class Equipment(
    val equipmentID: String,
    val equipmentName: String,
    val location: String = "",
    val status: String = STATUS_OPERATIONAL,
)

/** The states an asset can be in. */
const val STATUS_OPERATIONAL: String = "Operational"
const val STATUS_DUE_SOON: String = "Due soon"
const val STATUS_OVERDUE: String = "Overdue"
const val STATUS_UNDER_REPAIR: String = "Under repair"

val EQUIPMENT_STATUSES: List<String> = listOf(
    STATUS_OPERATIONAL,
    STATUS_DUE_SOON,
    STATUS_OVERDUE,
    STATUS_UNDER_REPAIR,
)
