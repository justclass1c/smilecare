package com.class1c.smilecare.features.staff.accountformscreen.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.em
import com.class1c.smilecare.features.auth.register.presentation.RegisterEvent
import com.class1c.smilecare.features.auth.register.presentation.RegisterField
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTabs
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusTone

/**
 * Reception add/edit user form. Role selection is hoisted so the screens
 * remain stateless.
 */
@Composable
fun StaffAccountFormScreen(
    state: StaffAccountFormUIState,
    onEvent: (StaffAccountFormEvent) -> Unit,
    isEditing: Boolean = false,
    showError: Boolean = false,
    onRoleChange: (Int) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    SmileScreen(
        title = if (isEditing) "Edit user" else "Add user",
        onBackClick = onBackClick,
    ) {
        Text(
            text = if (isEditing) {
                "Keep every detail current."
            } else {
                "A new face. The same care."
            },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = if (isEditing) {
                "Update the details below, then save your changes."
            } else {
                "Create an account with the right clinic access."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "Fields marked * are required.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (showError) {
            NoticeCard(
                text = "Please check the highlighted fields below.",
                title = "A few details need your attention",
                tone = StatusTone.DANGER,
            )
        }

        Text(
            text = "Account role",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SmileTabs(
            options = listOf("Patient", "Dentist", "Nurse"),
            selectedIndex = state.roleIndex,
            onSelect = { onEvent(StaffAccountFormEvent.UpdateRoleIndex(it)) },
        )

        SectionTitle(title = "Account details")

        SmileTextField(
            label = "Full name *",
            value = state.fullName,
            placeholder = "Full Name",
            onValueChange = { onEvent(StaffAccountFormEvent.UpdateFullName(it)) },
            onBlur = { onEvent(StaffAccountFormEvent.ValidateInput(StaffAccountFormField.NAME)) },
            isError = state.invalidName != null,
            errorText = state.invalidName
        )

        SmileTextField(
            label = "Date of Birth *",
            value = state.dateOfBirth,
            placeholder = "DD/MM/YYYY",
            onValueChange = { onEvent(StaffAccountFormEvent.UpdateDateOfBirth(it)) },
            onBlur = { onEvent(StaffAccountFormEvent.ValidateInput(StaffAccountFormField.DATEOFBIRTH)) },
            isError = state.invalidDateOfBirth != null,
            errorText = state.invalidDateOfBirth
        )

        SmileTabs(
            options = listOf("Female", "Male"),
            selectedIndex = state.genderIndex,
            onSelect = { onEvent(StaffAccountFormEvent.UpdateGender(it)) },
        )

        SmileTextField(
            label = "NRIC",
            value = state.nric,
            onBlur = { onEvent(StaffAccountFormEvent.ValidateInput(StaffAccountFormField.NRIC)) },
            onValueChange = { onEvent(StaffAccountFormEvent.UpdateNRIC(it)) },
            placeholder = "000000-00-0000",
            isError = state.invalidNric != null,
            errorText = state.invalidNric,
        )

        SmileTextField(
            label = "Email *",
            value = state.email,
            placeholder = "email@example.com",
            onValueChange = { onEvent(StaffAccountFormEvent.UpdateEmail(it)) },
            onBlur = { onEvent(StaffAccountFormEvent.ValidateInput(StaffAccountFormField.EMAIL)) },
            isError = state.invalidEmail != null,
            errorText = state.invalidEmail,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        SmileTextField(
            label = "Phone Number*",
            value = state.phoneNo,
            placeholder = "012-345 6789",
            onValueChange = { onEvent(StaffAccountFormEvent.UpdatePhoneNo(it)) },
            onBlur = { onEvent(StaffAccountFormEvent.ValidateInput(StaffAccountFormField.PHONENO)) },
            isError = state.invalidPhoneNo != null,
            errorText = state.invalidPhoneNo,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )
        if (state.roleIndex == 0) {
            SmileTextField(
                label = "Allergies",
                value = state.allergies,
                onValueChange = { onEvent(StaffAccountFormEvent.UpdateAllergies(it)) },
                isError = state.invalidSpecialization != null,
                errorText = state.invalidSpecialization,
                placeholder = "Leave blank if none.",
            )

            SmileTextField(
                label = "Medical Notes",
                value = state.medicalNotes,
               //  onValueChange = { onEvent(StaffAccountFormEvent.UpdateMedicalNotes(it)) },
                isError = state.invalidSpecialization != null,
                errorText = state.invalidSpecialization,
                placeholder = "Leave blank if none.",
            )
        } else if (state.roleIndex == 1) {
            SmileTextField(
                label = "Specialisation",
                value = state.specialization,
                onValueChange = { onEvent(StaffAccountFormEvent.UpdateSpecialization(it)) },
                onBlur = { onEvent(StaffAccountFormEvent.ValidateInput(StaffAccountFormField.SPECIALTY)) },
                isError = state.invalidSpecialization != null,
                errorText = state.invalidSpecialization,
                placeholder = "General Dentistry",
            )
        }
        SmileTextField(
            label = if (isEditing) "Password" else "Password *",
            value = state.password,
            onValueChange = { onEvent(StaffAccountFormEvent.UpdatePassword(it)) },
            placeholder =
                if (isEditing) "Leave blank to keep the current password"
                else "At least 8 characters with a number",
            onBlur = { onEvent(StaffAccountFormEvent.ValidateInput(StaffAccountFormField.PASSWORD)) },
            isError = state.invalidPassword != null,
            errorText = state.invalidPassword,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
        )

        SmileTextField(
            label = if (isEditing) "Confirm password" else "Confirm password *",
            value = state.confirmPassword,
            onValueChange = { onEvent(StaffAccountFormEvent.UpdateConfirmPassword(state.password, it)) },
            onBlur = { onEvent(StaffAccountFormEvent.ValidateInput(StaffAccountFormField.CONFIRMPASSWORD)) },
            placeholder = "Re-enter your password",
            isError = state.invalidConfirmPassword != null,
            errorText = state.invalidConfirmPassword,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
        )

        Text(
            text = "The password must include:\n" +
                    "- At least 8 characters\n" +
                    "- At least a number\n" +
                    "- At least a special character",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 3.em
        )

        SmileCard {
            PrimaryButton(
                text = if (isEditing) "Save changes" else "Create",
                onClick = {
                    if (isEditing) onEvent(StaffAccountFormEvent.SaveAccountChanges)
                    else onEvent(StaffAccountFormEvent.RegisterAccount)
                },
                leadingIcon = Icons.Filled.Check,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Your personal information stays private and secure.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        state.successMessage?.let { message ->
            AlertDialog( // TODO: Design to match
                onDismissRequest = { onEvent(StaffAccountFormEvent.DismissSuccess) },
                title = { Text("Registration Successful!") },
                text = { Text(message) },
                confirmButton = {
                    TextButton(onClick = { onEvent(StaffAccountFormEvent.DismissSuccess) }) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }
}
