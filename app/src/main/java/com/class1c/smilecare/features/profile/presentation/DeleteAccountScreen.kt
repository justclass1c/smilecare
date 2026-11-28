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
import com.class1c.smilecare.ui.components.Avatar
import com.class1c.smilecare.ui.components.DetailRow
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.RoleBadge
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileAlertDialog
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileDivider
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.StatusTone

/**
 * Delete-account confirmation rendered over the profile summary. The password
 * field is part of the dialog, not screen state.
 */
@Composable
fun DeleteAccountScreen(
    role: UserRole = UserRole.PATIENT,
    showDialog: Boolean = true,
    onBackClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    SmileScreen(
        title = "Profile",
        onBackClick = onBackClick,
    ) {
        SmileCard {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Avatar(initials = "PT", size = 76.dp)
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
            DetailRow(label = "NRIC", value = "000000-00-0000")
        }

        NoticeCard(
            text = "Deleting your account removes access but keeps required clinic records.",
        )
    }

    if (showDialog) {
        SmileAlertDialog(
            title = "Delete your account?",
            message = "Enter your password to confirm it's you.",
            confirmLabel = "Delete account",
            dismissLabel = "Keep account",
            tone = StatusTone.DANGER,
            onConfirmClick = onDeleteClick,
            onDismissClick = onCancelClick,
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
}
