package com.class1c.smilecare.features.auth.register.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTabs
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.SuccessBanner
import com.class1c.smilecare.ui.components.TextAction

/**
 * Registration screen. The same destination serves every role; the extra
 * fields are chosen from [role]. State is hoisted to the caller.
 */
@Composable
fun RegisterScreen(
    state: RegisterUIState,
    role: UserRole = UserRole.PATIENT,
    onEvent: (RegisterEvent) -> Unit,
    showError: Boolean = false,
    showSuccess: Boolean = false,
    onRegisterClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    SmileScreen(
        title = "Create account",
        onBackClick = onBackClick,
    ) {
        Text(
            text = "Your healthier smile starts here.",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "Create an account with the right clinic details.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (showError) {
            NoticeCard(
                text = "Error text",
                title = "Error text",
                tone = StatusTone.DANGER,
            )
        }
        if (showSuccess) {
            SuccessBanner("Account created successfully")
        }

        SmileTextField(
            label = "Full name",
            value = state.fullName,
            // pass value to event using it.
            // it = iterator, refers to current value of the field
            onValueChange = { onEvent(RegisterEvent.UpdateFullName(it)) },
            onBlur = { onEvent(RegisterEvent.ValidateInput(RegisterField.NAME)) },
            placeholder = "Full name",
            isError = state.invalidName != null,
            errorText = state.invalidName,
        )

        SmileTextField(
            label = "Date of birth",
            value = state.dateOfBirth,
            // onBlur is a custom parameter manually defined in SmileCareComponents.kt
            // it is an event that detects when focus on an input field leaves.
            onBlur = { onEvent(RegisterEvent.ValidateInput(RegisterField.DATEOFBIRTH)) },
            onValueChange = { onEvent(RegisterEvent.UpdateDateOfBirth(it)) },
            placeholder = "DD/MM/YY",
            isError = state.invalidDateOfBirth != null,
            errorText = state.invalidDateOfBirth,
        )

        Text(
            text = "Gender",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        SmileTabs(
            options = listOf("Female", "Male"),
            selectedIndex = state.genderIndex,
            onSelect = { onEvent(RegisterEvent.UpdateGender(it)) },
        )

        SmileTextField(
            label = "NRIC",
            value = state.nric,
            onBlur = { onEvent(RegisterEvent.ValidateInput(RegisterField.NRIC)) },
            onValueChange = { onEvent(RegisterEvent.UpdateNRIC(it)) },
            placeholder = "000000-00-0000",
            isError = state.invalidNric != null,
            errorText = state.invalidNric,
        )

        SmileTextField(
            label = "Email",
            value = state.email,
            onBlur = { onEvent(RegisterEvent.ValidateInput(RegisterField.EMAIL)) },
            onValueChange = { onEvent(RegisterEvent.UpdateEmail(it)) },
            placeholder = "email@example.com",
            isError = state.invalidEmail != null,
            errorText = state.invalidEmail,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        SmileTextField(
            label = "Phone",
            value = state.phoneNo,
            onBlur = { onEvent(RegisterEvent.ValidateInput(RegisterField.PHONENO)) },
            onValueChange = { onEvent(RegisterEvent.UpdatePhoneNo(it)) },
            placeholder = "000-000 0000",
            isError = state.invalidPhoneNo != null,
            errorText = state.invalidPhoneNo,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )

        SmileTextField(
            label = "Allergies (optional)",
            value = state.allergies,
            onValueChange = { onEvent(RegisterEvent.UpdateAllergies(it)) },
            onBlur = { onEvent(RegisterEvent.ValidateInput(RegisterField.ALLERGIES)) },
            placeholder = "Allergies",
            isError = state.invalidAllergies != null,
            errorText = state.invalidAllergies,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f).padding(end = 16.dp)) {
                Text(
                    text = "Dental Insurance Available?",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                )

                Text( // TODO: what is this?
                    text = "Optional cover details",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Switch(
                checked = state.hasInsurance,
                onCheckedChange = { onEvent(RegisterEvent.ToggleHasInsurance) },
            )
        }

        SmileTextField(
            label = "Password",
            value = state.password,
            onBlur = { onEvent(RegisterEvent.ValidateInput(RegisterField.PASSWORD)) },
            onValueChange = { onEvent(RegisterEvent.UpdatePassword(it)) },
            placeholder = "At least 8 characters with a number",
            isError = state.invalidPassword != null,
            errorText = state.invalidPassword,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
        )

        SmileTextField(
            label = "Confirm password",
            value = state.confirmPassword,
            onBlur = { onEvent(RegisterEvent.ValidateInput(RegisterField.CONFIRMPASSWORD)) },
            onValueChange = { onEvent(RegisterEvent.UpdateConfirmPassword(state.password, it)) },
            placeholder = "Re-enter your password",
            isError = state.invalidConfirmPassword != null,
            errorText = state.invalidConfirmPassword,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
        )

        Text(
            text = "Your password must include:\n" +
                    "- At least 8 characters\n" +
                    "- At least a number\n" +
                    "- At least a special character",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 3.em
        )

        // TODO: doesnt exit out of screen upon completion
        PrimaryButton(text = "Create account", onClick = { onEvent(RegisterEvent.RegisterAccount) })

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Already have an account?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            TextAction(text = "Login", onClick = onBackClick)
        }
    }
}
