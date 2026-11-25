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
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.RoleBadge
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileDivider
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.TextAction

/**
 * Staff account detail. Reception can edit or delete; the account identity is
 * placeholder content until the ViewModel layer exists.
 */
@Composable
fun StaffAccountDetailScreen(
    staffId: String = "staff-1",
    staffRole: UserRole = UserRole.DENTIST,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
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
                    Text(
                        text = "Staff ID · $staffId",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    RoleBadge(role = staffRole)
                }
            }
        }

        SectionTitle(title = "Contact & account details")
        SmileCard {
            DetailRow(label = "Full name", value = "Staff name")
            SmileDivider()
            DetailRow(label = "Email", value = "email@example.com")
            SmileDivider()
            DetailRow(label = "Phone", value = "000-000 0000")
            SmileDivider()
            DetailRow(label = "NRIC", value = "000000-00-0000")
            SmileDivider()
            if (staffRole == UserRole.DENTIST) {
                DetailRow(label = "Specialisation", value = "Error text")
                SmileDivider()
            }
            DetailRow(
                label = "Account status",
                value = "Active",
            )
        }

        NoticeCard(
            text = "Staff account changes are logged for clinic security.",
        )

        PrimaryButton(text = "Edit", onClick = onEditClick)
        TextAction(
            text = "Delete",
            onClick = onDeleteClick,
            color = MaterialTheme.colorScheme.error,
        )
    }
}
