package com.class1c.smilecare.features.auth.register.presentation

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.class1c.smilecare.domain.model.user.Dentist
import com.class1c.smilecare.domain.model.user.Patient
import com.class1c.smilecare.domain.model.user.User
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    // private one ensures only ViewModel can modify it// private one ensures only ViewModel can modify it. holds current state
    private val _uiState = MutableStateFlow(RegisterUIState())
    // readonly StateFlow. UI can only see this and render what is in here.
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    fun onEvent(event: RegisterEvent) = when(event) {
        RegisterEvent.ToggleHasInsurance -> _uiState.update { it.copy(hasInsurance = !it.hasInsurance)}
        is RegisterEvent.UpdateAllergies -> _uiState.update { it.copy(allergies = event.allergies) }
        is RegisterEvent.UpdateConfirmPassword -> _uiState.update { it.copy(confirmPassword = event.confirmPassword) }
        is RegisterEvent.UpdateDateOfBirth -> _uiState.update { it.copy(dateOfBirth = event.dateOfBirth) }
        is RegisterEvent.UpdateEmail -> _uiState.update { it.copy(email = event.email) }
        is RegisterEvent.UpdateFullName -> _uiState.update { it.copy(fullName = event.fullName) }
        is RegisterEvent.UpdateGender -> _uiState.update { it.copy(genderIndex = event.genderIndex) }
        is RegisterEvent.UpdateNRIC -> _uiState.update { it.copy(nric = event.nric) }
        is RegisterEvent.UpdatePassword -> _uiState.update { it.copy(password = event.password) }
        is RegisterEvent.UpdatePhoneNo -> _uiState.update { it.copy(phoneNo = event.phoneNo) }
        is RegisterEvent.UpdateSpecialization -> _uiState.update { it.copy(specialization = event.specialization) }
        is RegisterEvent.ValidateInput -> _uiState.update {
            when(event.field) {
                RegisterField.NAME             -> it.copy(invalidName             = validateName(it.fullName))
                RegisterField.DATEOFBIRTH      -> it.copy(invalidDateOfBirth      = validateDateOfBirth(it.dateOfBirth))
                RegisterField.GENDER           -> it.copy(invalidGender           = validateGender(it.genderIndex))
                RegisterField.NRIC             -> it.copy(invalidNric             = validateNric(it.nric))
                RegisterField.EMAIL            -> it.copy(invalidEmail            = validateEmail(it.email))
                RegisterField.PHONENO          -> it.copy(invalidPhoneNo          = validatePhoneNo(it.phoneNo))
                RegisterField.PASSWORD         -> it.copy(invalidPassword         = validatePassword(it.password))
                RegisterField.CONFIRMPASSWORD  -> it.copy(invalidConfirmPassword   = validateConfirmPassword(it.password, it.confirmPassword))
                RegisterField.ALLERGIES        -> it.copy(invalidAllergies = validateAllergies(it.allergies))
                RegisterField.MEDICALNOTES        -> it.copy(invalidMedicalNotes = validateMedicalNotes(it.medicalNotes))
                RegisterField.SPECIALTY -> it.copy(invalidSpecialization = validateSpecialization(it.specialization))
            }
        }
        RegisterEvent.RegisterAccount -> {
            val user = _uiState.value
            // run the validation check again
            val nameErr = validateName(user.fullName)
            val dateOfBirthErr = validateDateOfBirth(user.dateOfBirth)
            val nricErr = validateNric(user.nric)
            val emailErr = validateEmail(user.email)
            val phoneNoErr = validatePhoneNo(user.phoneNo)
            val passwordErr = validatePassword(user.password)
            val confirmPasswordErr = validateConfirmPassword(user.password, user.confirmPassword)

            val allValid = nameErr == null && dateOfBirthErr == null && nricErr == null && emailErr == null && phoneNoErr == null && passwordErr == null && confirmPasswordErr == null

            // if not valid, update ui state to display
            if (!allValid) {
                _uiState.update { it.copy(
                    invalidName = nameErr,
                    invalidDateOfBirth = dateOfBirthErr,
                    invalidNric = nricErr,
                    invalidEmail = emailErr,
                    invalidPhoneNo = phoneNoErr,
                    invalidPassword = passwordErr,
                    invalidConfirmPassword = confirmPasswordErr
                )}
            } else {
                viewModelScope.launch {
                    if (user.role == UserRole.PATIENT) {
                        userRepository.registerUser(
                            Patient(
                                id = UUID.randomUUID().toString(),
                                name = user.fullName,
                                email = user.email,
                                password = user.password,
                                nric = user.nric,
                                phoneNo = user.phoneNo,
                                dateOfBirth = user.dateOfBirth,
                                hasInsurance = user.hasInsurance,
                                gender = if (user.genderIndex == 0) 'F' else 'M',
                                allergies = user.allergies,
                                medicalNotes = user.medicalNotes,
                            )
                        )
                    }
                }

                _uiState.update { it.copy(isRegistered = !it.isRegistered) }

            }
        }
    }

    // ---- field validation ----
    // Stubs: every body returns null = "no error" for now. Replace each body with real rules.
    // Convention: return a short user-facing message when invalid, null when valid.

    // common fields
    private fun validateName(fullName: String): String? {
        if (fullName.isEmpty()) return "Name cannot be empty."
        else if (fullName.length > 50) return "Name cannot be longer than 50 characters."
        return null
    }
    private fun validateDateOfBirth(dateOfBirth: String): String? {
        val dateOfBirthFormat = Regex("""^(?:(?:0[1-9]|[12]\d|3[01])/(?:0[13578]|1[02])|(?:0[1-9]|[12]\d|30)/(?:0[469]|11)|(?:0[1-9]|1\d|2[0-8])/02)/\d{4}$|^(?:29/02/(?:[02468][048]|[13579][26]))$""")
        if (dateOfBirth.isEmpty()) return "Date of Birth cannot be empty."
        else if (dateOfBirth.matches(dateOfBirthFormat)) return null
        else return "Invalid date. Apply the format. Example: 24/12/2000"
    }
    private fun validateGender(genderIndex: Int): String? {
        return null
    }
    private fun validateNric(nric: String): String? {
        val nricFormat = Regex("""^\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])-\d{2}-\d{4}$""")
        // TODO: add duplicate check
        if (nric.isEmpty()) return "NRIC cannot be empty."
        else if (nric.matches(nricFormat)) return null
        else return "Invalid NRIC. Apply the format. Example: 950321-12-0011"
    }
    private fun validateEmail(email: String): String? {
        val emailFormat = Patterns.EMAIL_ADDRESS//Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$""")
        // TODO: add duplicate check
        if (email.isEmpty()) return "Email cannot be empty."
        else if (emailFormat.matcher(email).matches()) return null
        else return "Invalid Email."
    }
    private fun validatePhoneNo(phoneNo: String): String? {
        // TODO: phone number format should be 012-345 6789
        val phoneNoFormat = Regex("""^01\d-\d{7,8}$""")
        // TODO: add duplicate check
        if (phoneNo.isEmpty()) return "Phone number cannot be empty."
        else if (phoneNo.matches(phoneNoFormat)) return null
        else return "Invalid phone number."
    }
    private fun validatePassword(password: String): String? {
        val passwordFormat = Regex("""^(?=.*[0-9])(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,}$""")
        if (password.isEmpty()) return "Password must not be empty."
        else if (password.matches(passwordFormat)) return null
        else return "Invalid password."
    }
    private fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        if (password == confirmPassword) return null
        return "Passwords do not match."
    }

    // role-exclusive fields (invoked from RegisterAccount's validateAll step, not from blur)
    // den
    private fun validateSpecialization(specialization: String): String? {
        if (specialization.isEmpty()) return "Specify specialization."
        else if (specialization.length > 50) return "Cannot be longer than 50 characters."
        return null
    }

    private fun validateAllergies(allergies: String): String? {
        if (allergies.length > 50) return "Cannot be longer than 50 characters."
        return null
    }

    private fun validateMedicalNotes(medicalNotes: String): String? {
        if (medicalNotes.length > 50) return "Cannot be longer than 50 characters."
        return null
    }
    private fun validateEmployeeID(employeeID: String): String? = null

}