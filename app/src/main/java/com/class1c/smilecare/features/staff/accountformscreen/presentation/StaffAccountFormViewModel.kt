package com.class1c.smilecare.features.staff.accountformscreen.presentation

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.class1c.smilecare.domain.model.user.Dentist
import com.class1c.smilecare.domain.model.user.Patient
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.domain.repository.UserRepository
import com.class1c.smilecare.features.auth.register.presentation.RegisterEvent
import com.class1c.smilecare.features.auth.register.presentation.RegisterField
import com.class1c.smilecare.features.auth.register.presentation.RegisterUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class StaffAccountFormViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    // private one ensures only ViewModel can modify it// private one ensures only ViewModel can modify it. holds current state
    private val _uiState = MutableStateFlow(StaffAccountFormUIState())
    // readonly StateFlow. UI can only see this and render what is in here.
    val uiState: StateFlow<StaffAccountFormUIState> = _uiState.asStateFlow()

    fun onEvent(event: StaffAccountFormEvent) = when(event) {
         StaffAccountFormEvent.ToggleHasInsurance -> _uiState.update { it.copy(hasInsurance = !it.hasInsurance)}
        is StaffAccountFormEvent.UpdateAllergies -> _uiState.update { it.copy(allergies = event.allergies) }
        is StaffAccountFormEvent.UpdateConfirmPassword -> _uiState.update { it.copy(confirmPassword = event.confirmPassword) }
        is StaffAccountFormEvent.UpdateDateOfBirth -> _uiState.update { it.copy(dateOfBirth = event.dateOfBirth) }
        is StaffAccountFormEvent.UpdateEmail -> _uiState.update { it.copy(email = event.email) }
        is StaffAccountFormEvent.UpdateFullName -> _uiState.update { it.copy(fullName = event.fullName) }
        is StaffAccountFormEvent.UpdateGender -> _uiState.update { it.copy(genderIndex = event.genderIndex) }
        is StaffAccountFormEvent.UpdateNRIC -> _uiState.update { it.copy(nric = event.nric) }
        is StaffAccountFormEvent.UpdatePassword -> _uiState.update { it.copy(password = event.password) }
        is StaffAccountFormEvent.UpdatePhoneNo -> _uiState.update { it.copy(phoneNo = event.phoneNo) }
        is StaffAccountFormEvent.UpdateSpecialization -> _uiState.update { it.copy(specialization = event.specialization) }
        is StaffAccountFormEvent.UpdateRoleIndex -> _uiState.update { it.copy(roleIndex = event.roleIndex)}
        is StaffAccountFormEvent.ValidateInput -> _uiState.update {
            when(event.field) {
                 StaffAccountFormField.NAME             -> it.copy(invalidName             = validateName(it.fullName))
                 StaffAccountFormField.DATEOFBIRTH      -> it.copy(invalidDateOfBirth      = validateDateOfBirth(it.dateOfBirth))
                 StaffAccountFormField.GENDER           -> it.copy(invalidGender           = validateGender(it.genderIndex))
                 StaffAccountFormField.NRIC             -> it.copy(invalidNric             = validateNric(it.nric))
                 StaffAccountFormField.EMAIL            -> it.copy(invalidEmail            = validateEmail(it.email))
                 StaffAccountFormField.PHONENO          -> it.copy(invalidPhoneNo          = validatePhoneNo(it.phoneNo))
                 StaffAccountFormField.PASSWORD         -> it.copy(invalidPassword         = validatePassword(it.password))
                 StaffAccountFormField.CONFIRMPASSWORD  -> it.copy(invalidConfirmPassword   = validateConfirmPassword(it.password, it.confirmPassword))
                 StaffAccountFormField.SPECIALTY -> it.copy(invalidSpecialization = validateSpecialization(it.specialization))
            }
        }
        StaffAccountFormEvent.RegisterAccount -> {
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
                    when (user.roleIndex) {
                        0 -> userRepository.registerUser(
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

                        1 -> userRepository.registerUser(
                            Dentist(
                                id = UUID.randomUUID().toString(),
                                name = user.fullName,
                                email = user.email,
                                password = user.password,
                                nric = user.nric,
                                phoneNo = user.phoneNo,
                                dateOfBirth = user.dateOfBirth,
                                gender = if (user.genderIndex == 0) 'F' else 'M',
                                specialty = user.specialization,
                            )
                        )

                        else -> null
                    }
                }

                _uiState.update { it.copy(isRegistered = !it.isRegistered) }

            }
        }
        StaffAccountFormEvent.SaveAccountChanges -> TODO()
    }

    // ---- field validation ----
    // Stubs: every body returns null = "no error" for now. Replace each body with real rules.
    // Convention: return a short user-facing message when invalid, null when valid.

    // common fields
    private fun validateName(fullName: String): String? {
        if (fullName.isEmpty()) return "Name cannot be empty."
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
    private fun validateSpecialization(specialization: String): String? {
        if (specialization.isEmpty()) return "Specify specialization."
        return null
    }

    private fun validateEmployeeID(employeeID: String): String? = null
}