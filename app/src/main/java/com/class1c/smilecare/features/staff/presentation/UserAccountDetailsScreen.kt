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
import com.class1c.smilecare.domain.model.user.Dentist
import com.class1c.smilecare.domain.model.user.Nurse
import com.class1c.smilecare.domain.model.user.Patient
import com.class1c.smilecare.ui.components.Avatar
import com.class1c.smilecare.ui.components.DetailRow
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.RoleBadge
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileDivider
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.TextAction

/**
 * Staff account detail. The identity comes from [UserAccountDetailsUIState.user],
 * loaded by the ViewModel from the user id carried by the route.
 */
@Composable
fun UserAccountDetailsScreen(
    state: UserAccountDetailsUIState,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    SmileScreen(
        title = "User details",
        onBackClick = onBackClick,
    ) {
        state.error?.let { error ->
            NoticeCard(text = error, tone = StatusTone.DANGER)
        }

        if (state.isLoading) { // TODO: center
            Text(
                text = "Loading account…",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        state.user?.let { user ->
            SmileCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Avatar(initials = initials(user.name), size = 60.dp)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "User ID · ${state.user.id}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        RoleBadge(role = user.role)
                    }
                }
            }

            SectionTitle(title = "Contact & account details")
            SmileCard {
                DetailRow(label = "Full Name", value = user.name)
                SmileDivider()
                DetailRow(label = "Email", value = user.email)
                SmileDivider()
                DetailRow(label = "Phone", value = user.phoneNo)
                SmileDivider()
                DetailRow(label = "NRIC", value = user.nric)
                SmileDivider()
                when (user) {
                    is Dentist -> {
                        DetailRow(label = "Specialisation", value = user.specialty)
                        SmileDivider()
                        DetailRow(label = "Dentist ID", value = user.dentistId)
                    }
                    is Nurse -> {
                        DetailRow(label = "Nurse ID", value = user.nurseId)
                    }
                    is Patient -> {
                        DetailRow(label = "Date of birth", value = user.dateOfBirth)
                        SmileDivider()
                        DetailRow(label = "Allergies", value = if (user.allergies == "") "None" else user.allergies ?: "None")
                    }
                }
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
}

private fun initials(name: String): String = name
    .split(Regex("\\s+"))
    .filter { it.isNotBlank() }
    .take(2)
    .joinToString("") { it.first().uppercase() }
    .ifEmpty { "?" }
