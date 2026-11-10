package com.class1c.smilecare.data.repository

import com.class1c.smilecare.data.remote.row.PatientRow
import com.class1c.smilecare.data.remote.row.UserRow
import com.class1c.smilecare.domain.model.user.Dentist
import com.class1c.smilecare.domain.model.user.Patient
import com.class1c.smilecare.domain.model.user.Receptionist
import com.class1c.smilecare.domain.model.user.User
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.domain.repository.UserRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // only one instance of this should run throughout the entire lifecycle of the app
class UserRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient
): UserRepository {

    override suspend fun registerUser(user: User): String {
        // Insert general user credentials first

        val row = supabase.from("users").insert(
            UserRow(
                userId = user.id,
                name = user.name,
                email = user.email,
                password = user.password,
                nric = user.nric,
                phoneNo = user.phoneNo,
                dateOfBirth = LocalDate.parse(user.dateOfBirth, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    .run { format(DateTimeFormatter.ISO_LOCAL_DATE) },
                gender = user.gender.toString().uppercase(),
                role = when(user) {
                    is Patient -> "patient"
                    is Dentist -> "dentist"
                    is Receptionist-> "receptionist"
                }
            )
        ) { select() }.decodeSingle<UserRow>()

        // get userID from supabase
        val userId = row.userId ?: error("Error: Supabase returned no user_id")

        when(user) { // using user instead of user.role for smart casting.
            is Patient -> supabase.from("patients")
                // map column header to variable
                .insert(
                    PatientRow(
                        userId = user.id,
                        hasInsurance = user.hasInsurance,
                        allergies = user.allergies ?: "",
                        medicalNotes = user.medicalNotes ?: ""
                    )
                )
            is Dentist -> supabase.from("dentists")
                .insert(mapOf("user_id" to userId, "specialty" to user.specialty))
            is Receptionist -> supabase.from("") // TODO: pending decision
        }

        return userId
    }

    override suspend fun loginUser(email: String, password: String): User? {
        val target = supabase.from("users").select(
            columns = Columns.list("user_id", "name", "email", "password", "nric", "phone_no", "date_of_birth", "gender", "role")
        ) {
            filter {
                eq("email", email)
            }
        }.decodeSingleOrNull<UserRow>()
            ?.takeIf { it.password == password }

        when(target?.role) {
            "patient" -> {
                val patient = supabase.from("patients").select(
                    columns = Columns.list("user_id", "has_insurance", "allergies", "medical_notes")
                ) {
                    filter {
                        eq ("user_id", target.userId)
                    }
                }.decodeSingleOrNull<PatientRow>() ?: return null

                return Patient(
                    id = target.userId,
                    name = target.name,
                    email = target.email,
                    password = target.password,
                    nric = target.nric,
                    phoneNo = target.phoneNo,
                    dateOfBirth = target.dateOfBirth,
                    gender = target.gender.first().uppercaseChar(),
                    hasInsurance = patient.hasInsurance,
                    allergies = patient.allergies,
                    medicalNotes = patient.medicalNotes
                )
            }
        }

        return null
    }

    override suspend fun updateUser(user: User) {
    }

    override suspend fun deleteUser(user: User) {
    }

}

