package com.class1c.smilecare.domain.model

/**
 * Pure Kotlin model owned by the domain layer.
 *
 * It has no Android or database annotations, so the domain layer never depends
 * on the data layer. Mapping happens at the data-layer edge.
 */
data class Dentist(
    val id: String,
    val name: String,
    val specialty: String,
    val rating: Double,
    val yearsOfExperience: Int,
    val isFavorite: Boolean = false,
)
