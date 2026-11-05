package com.class1c.smilecare.data.mapper

import com.class1c.smilecare.data.local.DentistEntity
import com.class1c.smilecare.domain.model.Dentist

/**
 * Data -> domain mapping. This is the only direction allowed: the domain layer
 * must never import anything from `data`.
 */
fun DentistEntity.toDomain(): Dentist = Dentist(
    id = id,
    name = fullName,
    specialty = specialty,
    rating = rating,
    yearsOfExperience = experienceInYears,
    isFavorite = favorite,
)
