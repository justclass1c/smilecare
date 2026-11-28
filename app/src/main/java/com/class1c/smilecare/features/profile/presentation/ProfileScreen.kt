package com.class1c.smilecare.features.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.user.Dentist
import com.class1c.smilecare.domain.model.user.Nurse
import com.class1c.smilecare.domain.model.user.Patient
import com.class1c.smilecare.domain.model.user.User
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.navigation.Route
import com.class1c.smilecare.ui.components.Avatar
import com.class1c.smilecare.ui.components.DetailRow
import com.class1c.smilecare.ui.components.LogoutAction
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.OutlineButton
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.RoleBadge
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileAlertDialog
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileDivider
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.TextAction

/**
 * Profile for every role. Dentists have read-only details; patients and
 * reception can edit their own information.
 */
@Composable
fun ProfileScreen(
    state: ProfileUIState,
    showDeleteDialog: Boolean = false,
    onEditProfileClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {},
    onConfirmDeleteClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    val user: User? = state.user

    SmileScreen(
        role = state.user?.role,
        currentRoute = Route.Profile,
        onNavigate = onNavigate,
    ) {
        SmileCard {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Avatar(
                    initials = initials(state.user?.name.orEmpty()),
                    size = 76.dp,
                )
                Text(
                    text = user?.name ?: "Guest",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                RoleBadge(role = state.user?.role)
            }
        }

        SectionTitle(title = "Personal information")
        SmileCard {
            DetailRow(label = "Full name", value = user?.name ?: "Guest")
            SmileDivider()
            DetailRow(label = "Email", value = user?.email ?: "Not logged in")
            SmileDivider()
            DetailRow(label = "Phone", value = user?.phoneNo ?: "Not logged in")
            SmileDivider()
            when (user) {
                is Patient -> {
                    DetailRow(label = "Date of birth", value = user.dateOfBirth ?: "Not logged in")
                    SmileDivider()
                    DetailRow(label = "Gender", value = user.gender.toString()) // TODO: Write out full gender
                    SmileDivider()
                    DetailRow(label = "NRIC", value = user.nric)
                    SmileDivider()
                    DetailRow(label = "Allergies", value = if (user.allergies == "") "None" else user.allergies ?: "")
                    SmileDivider()
                    DetailRow(label = "Medical Notes", value = if (user.medicalNotes == "") "None" else user.medicalNotes ?: "")
                }

                is Dentist -> {
                    DetailRow(label = "Specialisation", value = user.specialty)
                    SmileDivider()
                    DetailRow(label = "Dentist ID", value = user.dentistId)
                }

                is Nurse -> {
                    DetailRow(label = "Nurse ID", value = user.nurseId)
                }

                null -> TODO()
            }
        }

        if (user is Dentist) {
            NoticeCard(
                text = "Your details are read-only. Contact reception if anything needs updating.",
                title = "Managed by reception",
            )
        }

        when (user) {
            is Patient -> {
                PrimaryButton(
                    text = "Edit profile",
                    onClick = onEditProfileClick,
                )
                OutlineButton(
                    text = "Change password",
                    onClick = onChangePasswordClick,
                )
                TextAction(
                    text = "Delete account",
                    onClick = onDeleteAccountClick,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            is Dentist -> {
                OutlineButton(
                    text = "Change password",
                    onClick = onChangePasswordClick,
                )
            }

            is Nurse -> {
                PrimaryButton(
                    text = "Edit profile",
                    onClick = onEditProfileClick,
                )
                OutlineButton(
                    text = "Change password",
                    onClick = onChangePasswordClick,
                )
            }

            null -> TODO()
        }

        LogoutAction(onClick = onLogoutClick)

        if (showDeleteDialog) {
            SmileAlertDialog(
                title = "Delete your account?",
                message = "Enter your password to confirm it's you.",
                confirmLabel = "Delete account",
                dismissLabel = "Keep account",
                tone = StatusTone.DANGER,
                onConfirmClick = onConfirmDeleteClick,
                onDismissClick = onDeleteAccountClick,
                showPasswordField = true,
                extraContent = {
                    NoticeCard(
                        text = "You will lose access to your SmileCare account.",
                        title = "This cannot be undone",
                        tone = StatusTone.WARNING,
                    )
                },
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Your information. Handled with care.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun initials(name: String): String = name
    .trim()
    .split(Regex("\\s+"))
    .filter { it.isNotBlank() }
    .take(2)
    .joinToString("") { it.first().uppercase() }
    .ifEmpty { "?" }
