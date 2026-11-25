package com.class1c.smilecare.features.patients.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.navigation.Route
import com.class1c.smilecare.ui.components.EmptyState
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.PersonRow
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTabs
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusPill
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.TextAction

/**
 * Patient directory for dentists and the staff directory for reception.
 */
@Composable
fun PatientsScreen(
    role: UserRole,
    selectedTab: Int = 0,
    onTabChange: (Int) -> Unit = {},
    onPatientClick: (String) -> Unit = {},
    onAddStaffClick: () -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    val nurses = role == UserRole.NURSE
    SmileScreen(
        role = role,
        currentRoute = Route.Patients,
        title = if (nurses) "User management" else "Patients",
        onNavigate = onNavigate,
        floatingActionButton = {
            if (nurses) {
                ExtendedFloatingActionButton(
                    onClick = onAddStaffClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                        )
                    },
                    text = { Text("Add user") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
    ) {
        Text(
            text = if (nurses) {
                "Manage clinic access with clarity and care."
            } else {
                "The people behind every healthy smile."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SmileTabs(
            options = if (nurses) {
                listOf("Patients", "Dentists", "Staff")
            } else {
                listOf("All patients", "Recent", "Upcoming")
            },
            selectedIndex = selectedTab,
            onSelect = onTabChange,
        )
        SmileTextField(
            label = if (nurses) "Search staff accounts" else "Search name or NRIC",
            value = "",
            placeholder = "Search",
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "0 people",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            StatusPill(
                text = if (nurses) "Clinic directory" else "Read-only access",
                tone = StatusTone.INFO,
            )
        }

        if (nurses) {
            staffPreviews.forEach { person ->
                PersonRow(
                    initials = person.initials,
                    name = person.name,
                    subtitle = person.roleLabel,
                    onClick = { onPatientClick(person.id) },
                    trailing = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                )
            }
        } else {
            patientPreviews.forEach { person ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    PersonRow(
                        initials = person.initials,
                        name = person.name,
                        subtitle = person.subtitle,
                        onClick = { onPatientClick(person.id) },
                        trailing = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                    )
                }
            }
        }

        if (nurses) {
            NoticeCard(
                text = "Only reception can create, edit or remove staff accounts.",
            )
            TextAction(
                text = "Add user",
                onClick = onAddStaffClick,
                icon = Icons.Filled.Add,
            )
        } else {
            EmptyState(
                title = "No matching patients",
                message = "Try another name or NRIC. Patient records stay read-only.",
                icon = Icons.Filled.Person,
            )
        }

        Text(
            text = if (nurses) {
                "0 staff members"
            } else {
                "0 patients"
            },
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
        )
    }
}

private data class PersonPreview(
    val id: String,
    val initials: String,
    val name: String,
    val subtitle: String,
)

private val patientPreviews = listOf(
    PersonPreview("patient-1", "PN", "Patient name", "Last visit · DD/MM/YY"),
    PersonPreview("patient-2", "PN", "Patient name", "Last visit · DD/MM/YY"),
    PersonPreview("patient-3", "PN", "Patient name", "Last visit · DD/MM/YY"),
    PersonPreview("patient-4", "PN", "Patient name", "Last visit · DD/MM/YY"),
)

private data class StaffPreview(
    val id: String,
    val initials: String,
    val name: String,
    val roleLabel: String,
)

private val staffPreviews = listOf(
    StaffPreview("staff-1", "DN", "Staff name", "Dentist · Active"),
    StaffPreview("staff-2", "DN", "Staff name", "Dentist · Active"),
    StaffPreview("staff-3", "RC", "Staff name", "Reception · Active"),
    StaffPreview("staff-4", "RC", "Staff name", "Reception · Active"),
)
