package com.class1c.smilecare.features.patients.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
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
import com.class1c.smilecare.ui.components.OutlineButton
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.RoleBadge
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileDivider
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTabs
import com.class1c.smilecare.ui.components.StatusPill
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.TextAction

/**
 * Patient record seen by dentists (clinical, read-only) and reception
 * (administrative, editable). Clinical notes stay presentation-only.
 */
@Composable
fun PatientDetailScreen(
    role: UserRole,
    patientId: String = "patient-1",
    selectedTab: Int = 0,
    onTabChange: (Int) -> Unit = {},
    onBackClick: () -> Unit = {},
    onAddNoteClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onAppointmentClick: (String) -> Unit = {},
) {
    val nurses = role == UserRole.NURSE
    SmileScreen(
        title = "Patient details",
        onBackClick = onBackClick,
    ) {
        SmileCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Avatar(initials = "PN", size = 60.dp)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Patient name",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Patient ID · $patientId",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    RoleBadge(role = UserRole.PATIENT)
                }
            }
        }

        SectionTitle(title = "Contact & personal details")
        SmileCard {
            DetailRow(label = "Full name", value = "Patient name")
            SmileDivider()
            DetailRow(label = "Email", value = "email@example.com")
            SmileDivider()
            DetailRow(label = "Phone", value = "000-000 0000")
            SmileDivider()
            DetailRow(label = "NRIC", value = "000000-00-0000")
            SmileDivider()
            DetailRow(label = "Age", value = "00 years")
            SmileDivider()
            DetailRow(label = "Gender", value = "Error text")
            SmileDivider()
            DetailRow(label = "Allergies", value = "Error text")
            SmileDivider()
            DetailRow(label = "Dental insurance", value = "Error text")
        }

        if (nurses) {
            StatusPill(text = "SC-00000", tone = StatusTone.NEUTRAL)
            PrimaryButton(
                text = "Edit",
                onClick = onEditClick,
            )
            TextAction(
                text = "Delete",
                onClick = onDeleteClick,
                color = MaterialTheme.colorScheme.error,
            )
        } else {
            NoticeCard(
                text = "Error text — check before prescribing any medication.",
                title = "Allergy alert",
                tone = StatusTone.WARNING,
                icon = Icons.Filled.Warning,
            )
        }

        SmileTabs(
            options = listOf("History", "Treatment notes"),
            selectedIndex = selectedTab,
            onSelect = onTabChange,
        )

        if (selectedTab == 1) {
            SectionTitle(title = "Treatment notes")
            clinicalNotes.forEach { note ->
                ClinicalNoteCard(note = note)
            }
            if (role == UserRole.DENTIST) {
                OutlineButton(
                    text = "Add treatment note",
                    onClick = onAddNoteClick,
                    leadingIcon = Icons.Filled.Add,
                )
            }
        } else {
            SectionTitle(title = "Treatment history")
            historyAppointments.forEach { appointment ->
                SmileCard(onClick = { onAppointmentClick(appointment.id) }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = appointment.service,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = "${appointment.date} · Dentist name",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        StatusPill(
                            text = appointment.status,
                            tone = appointment.tone,
                        )
                    }
                }
            }

            SectionTitle(title = "Clinical notes")
            clinicalNotes.forEach { note ->
                ClinicalNoteCard(note = note)
            }
            if (role == UserRole.DENTIST) {
                PrimaryButton(
                    text = "Add treatment note",
                    onClick = onAddNoteClick,
                    leadingIcon = Icons.Filled.Add,
                )
            }
        }

        NoticeCard(
            text = "Patient records are private and handled with care.",
        )
    }
}

@Composable
private fun ClinicalNoteCard(note: ClinicalNote) {
    SmileCard {
        Text(
            text = "CLINICAL NOTES · ${note.date}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = note.title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = note.body,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = note.dentist,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private data class PatientHistory(
    val id: String,
    val service: String,
    val date: String,
    val status: String,
    val tone: StatusTone,
)

private data class ClinicalNote(
    val date: String,
    val title: String,
    val body: String,
    val dentist: String,
)

private val historyAppointments = listOf(
    PatientHistory(
        id = "APT-0001",
        service = "Service name",
        date = "DD/MM/YY",
        status = "Completed",
        tone = StatusTone.SUCCESS,
    ),
    PatientHistory(
        id = "APT-0002",
        service = "Service name",
        date = "DD/MM/YY",
        status = "Cancelled",
        tone = StatusTone.NEUTRAL,
    ),
)

private val clinicalNotes = listOf(
    ClinicalNote(
        date = "DD/MM/YY",
        title = "Routine dental examination",
        body = "Error text. Treatment findings and follow-up advice go here.",
        dentist = "Dentist name",
    ),
)
