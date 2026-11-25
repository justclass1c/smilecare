package com.class1c.smilecare.features.staff.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
 * Delete-user confirmation, shown over the staff detail summary.
 */
@Composable
fun DeleteStaffAccountScreen(
    showDialog: Boolean = true,
    onBackClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    SmileScreen(
        title = "User details",
        onBackClick = onBackClick,
    ) {
        SmileCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Avatar(initials = "SN", size = 60.dp)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Staff name",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    RoleBadge(role = UserRole.NURSE)
                }
            }
        }
        SectionTitle(title = "Contact & account details")
        SmileCard {
            DetailRow(label = "Email", value = "email@example.com")
            SmileDivider()
            DetailRow(label = "Phone", value = "000-000 0000")
            SmileDivider()
            DetailRow(label = "Account status", value = "Active")
        }
        NoticeCard(
            text = "Staff account changes are logged for clinic security.",
        )
    }

    if (showDialog) {
        SmileAlertDialog(
            title = "Delete Staff name?",
            message = "Their past appointments and payment records will be kept.",
            confirmLabel = "Delete",
            dismissLabel = "Cancel",
            tone = StatusTone.DANGER,
            onConfirmClick = onDeleteClick,
            onDismissClick = onCancelClick,
            extraContent = {
                NoticeCard(
                    text = "This removes their account access.",
                    tone = StatusTone.WARNING,
                )
            },
        )
    }
}
