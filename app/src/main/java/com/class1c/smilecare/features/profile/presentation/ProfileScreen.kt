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
    role: UserRole,
    showDeleteDialog: Boolean = false,
    onEditProfileClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {},
    onConfirmDeleteClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    SmileScreen(
        role = role,
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
                    initials = when (role) {
                        UserRole.PATIENT -> "PT"
                        UserRole.DENTIST -> "DN"
                        UserRole.NURSE -> "NR"
                    },
                    size = 76.dp,
                )
                Text(
                    text = "User name",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                RoleBadge(role = role)
            }
        }

        SectionTitle(title = "Personal information")
        SmileCard {
            DetailRow(label = "Full name", value = "User name")
            SmileDivider()
            DetailRow(label = "Email", value = "email@example.com")
            SmileDivider()
            DetailRow(label = "Phone", value = "000-000 0000")
            SmileDivider()
            when (role) {
                UserRole.PATIENT -> {
                    DetailRow(label = "Date of birth", value = "DD/MM/YY")
                    SmileDivider()
                    DetailRow(label = "Gender", value = "Error text")
                    SmileDivider()
                    DetailRow(label = "NRIC", value = "000000-00-0000")
                    SmileDivider()
                    DetailRow(label = "Allergies", value = "Error text")
                }

                UserRole.DENTIST -> {
                    DetailRow(label = "Specialisation", value = "Error text")
                }

                else -> {
                    DetailRow(label = "Employee ID", value = "Error text")
                    SmileDivider()
                    DetailRow(label = "Branch", value = "Error text")
                }
            }
        }

        if (role == UserRole.DENTIST) {
            NoticeCard(
                text = "Your details are read-only. Contact reception if anything needs updating.",
                title = "Managed by reception",
            )
        }

        when (role) {
            UserRole.PATIENT -> {
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

            UserRole.DENTIST -> {
                OutlineButton(
                    text = "Change password",
                    onClick = onChangePasswordClick,
                )
            }

            else -> {
                PrimaryButton(
                    text = "Edit profile",
                    onClick = onEditProfileClick,
                )
                OutlineButton(
                    text = "Change password",
                    onClick = onChangePasswordClick,
                )
            }
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
