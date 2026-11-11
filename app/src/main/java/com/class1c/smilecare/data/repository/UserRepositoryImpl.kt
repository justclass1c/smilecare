package com.class1c.smilecare.data.repository

import com.class1c.smilecare.data.remote.row.DentistRow
import com.class1c.smilecare.data.remote.row.NurseRow
import com.class1c.smilecare.data.remote.row.PatientRow
import com.class1c.smilecare.data.remote.row.UserRow
import com.class1c.smilecare.domain.model.user.Dentist
import com.class1c.smilecare.domain.model.user.Patient
import com.class1c.smilecare.domain.model.user.Nurse
import com.class1c.smilecare.domain.model.user.User
import com.class1c.smilecare.domain.repository.UserRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
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
                    is Nurse -> "nurse"
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
                        allergies = user.allergies ?: "N/A",
                        medicalNotes = user.medicalNotes ?: "N/A"
                    )
                )

            is Dentist -> supabase.from("dentists")
                .insert(
                    DentistRow(
                        userID = user.id,
                        dentistID = generateStaffId('D'),
                        specialty = user.specialty
                    )
                )

            is Nurse -> supabase.from("nurses")
                .insert(
                    NurseRow(
                        userID = user.id,
                        nurseID = generateStaffId('N'),
                    )
                )
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

            "dentist" -> {
                val dentist = supabase.from("dentists").select(
                    columns = Columns.list("user_id", "dentist_id", "specialty")
                ) {
                    filter {
                        eq ("user_id", target.userId)
                    }
                }.decodeSingleOrNull<DentistRow>() ?: return null

                return Dentist(
                    id = target.userId,
                    name = target.name,
                    email = target.email,
                    password = target.password,
                    nric = target.nric,
                    phoneNo = target.phoneNo,
                    dateOfBirth = target.dateOfBirth,
                    gender = target.gender.first().uppercaseChar(),
                    dentistId = dentist.dentistID,
                    specialty = dentist.specialty
                )
            }

            "nurse" -> {
                val nurse = supabase.from("nurses").select(
                    columns = Columns.list("user_id", "nurse_id")
                ) {
                    filter { eq("user_id", target.userId) }
                }.decodeSingleOrNull<NurseRow>() ?: return null

                return Nurse(
                    id = target.userId,
                    name = target.name,
                    email = target.email,
                    password = target.password,
                    nric = target.nric,
                    phoneNo = target.phoneNo,
                    dateOfBirth = target.dateOfBirth,
                    gender = target.gender.first().uppercaseChar(),
                    nurseId = nurse.nurseID
                )
            }
        }

        return null
    }

    override suspend fun updateUser(user: User) {
    }

    override suspend fun deleteUser(user: User) {
    }

    private suspend fun generateStaffId(prefix: Char): String {
        val lastId: String? = when (prefix) {
            'D' -> supabase.from("dentists").select(Columns.list("dentist_id")) {
                order("dentist_id", Order.DESCENDING)
                limit(1)
            }.decodeSingleOrNull<DentistRow>()?.dentistID

            else -> supabase.from("nurses").select(Columns.list("nurse_id")) {
                order("nurse_id", Order.DESCENDING)
                limit(1)
            }.decodeSingleOrNull<NurseRow>()?.nurseID
        }

        val next = lastId?.removePrefix("$prefix")?.toIntOrNull()?.plus(1) ?: 1
        return "$prefix%03d".format(next)
    }
}

