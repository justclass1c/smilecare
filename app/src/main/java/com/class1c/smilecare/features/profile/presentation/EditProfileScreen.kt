package com.class1c.smilecare.features.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTabs
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusTone

/**
 * Edit-profile form for patients and reception. Dentists' details are managed
 * by reception, so the same destinations simply omit the patient fields.
 */
@Composable
fun EditProfileScreen(
    role: UserRole = UserRole.PATIENT,
    showError: Boolean = false,
    genderIndex: Int = 0,
    insured: Boolean = true,
    onGenderChange: (Int) -> Unit = {},
    onInsuredChange: (Boolean) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    SmileScreen(
        title = "Edit profile",
        onBackClick = onBackClick,
    ) {
        Text(
            text = "A little update, a better visit.",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "Update the details below, then save your changes.",
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

        SmileTextField(
            label = "Full name *",
            value = "",
            placeholder = "Full name",
            isError = showError,
            errorText = if (showError) "Name must contain letters only" else null,
        )
        if (role == UserRole.PATIENT) {
            SmileTextField(
                label = "Age *",
                value = "",
                placeholder = "00",
                isError = showError,
                errorText = if (showError) "Age must be between 1 and 120" else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Text(
                text = "Gender *",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            SmileTabs(
                options = listOf("Male", "Female"),
                selectedIndex = genderIndex,
                onSelect = onGenderChange,
            )
            SmileTextField(
                label = "NRIC *",
                value = "",
                placeholder = "000000-00-0000",
                isError = showError,
                errorText = if (showError) "Invalid NRIC format" else null,
            )
        }
        SmileTextField(
            label = "Email *",
            value = "",
            placeholder = "email@example.com",
            isError = showError,
            errorText = if (showError) "Email already registered" else null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )
        SmileTextField(
            label = "Phone *",
            value = "",
            placeholder = "000-000 0000",
            isError = showError,
            errorText = if (showError) "Phone number is invalid" else null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )
        if (role == UserRole.DENTIST) {
            SmileTextField(
                label = "Specialisation",
                value = "",
                placeholder = "General Dentistry",
            )
        }
        if (role == UserRole.NURSE) {
            SmileTextField(
                label = "Employee ID",
                value = "",
                placeholder = "Error text",
            )
            SmileTextField(
                label = "Branch",
                value = "",
                placeholder = "Error text",
            )
        }
        SmileTextField(
            label = "Password",
            value = "",
            placeholder = "Leave blank to keep current password",
            isError = showError,
            errorText = if (showError) {
                "Password must be at least 8 characters with a number"
            } else {
                null
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
        )
        SmileTextField(
            label = "Confirm password",
            value = "",
            placeholder = "Confirm only if changing your password",
            isError = showError,
            errorText = if (showError) "Passwords do not match" else null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordTransformation,
        )

        if (role == UserRole.PATIENT) {
            SmileTextField(
                label = "Allergies (optional)",
                value = "",
                placeholder = "Write 'none' if no allergies",
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f).padding(end = 16.dp),
                ) {
                    Text(
                        text = "Dental insurance",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = "Optional cover details",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    checked = insured,
                    onCheckedChange = onInsuredChange,
                )
            }
        }

        PrimaryButton(
            text = "Save changes",
            onClick = onSaveClick,
            leadingIcon = Icons.Filled.Check,
        )
        Text(
            text = "Your personal information stays private and secure.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private val PasswordTransformation = PasswordVisualTransformation()
