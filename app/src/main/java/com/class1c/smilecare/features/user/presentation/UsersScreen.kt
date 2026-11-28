package com.class1c.smilecare.features.user.presentation

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.user.User
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

/**
 * Patient directory for dentists and the staff directory for reception.
 */
@Composable
fun UsersScreen(
    role: UserRole,
    state: UsersUIState,
    event: (UsersEvent) -> Unit,
    onPatientClick: (String) -> Unit = {},
    onAddStaffClick: () -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    val nurses = role == UserRole.NURSE
    val users = state.users

    SmileScreen(
        role = role,
        currentRoute = Route.Patients,
        title = if (nurses) "User Management" else "Patients",
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
                    text = { Text("Add User") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
    ) {
        state.error?.let { error ->
            NoticeCard(text = error, tone = StatusTone.DANGER)
        }
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
                listOf("Patients", "Dentists", "Nurses")
            } else {
                listOf("All patients", "Recent", "Upcoming")
            },
            selectedIndex = state.selectedTab,
            onSelect = { event(UsersEvent.SwitchTab(it) ) },
        )
        SmileTextField(
            label = if (nurses) "Search accounts" else "Search name or NRIC",
            value = state.searchQuery,
            onValueChange = { event(UsersEvent.UpdateSearchQuery(it)) },
            placeholder = "",
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = (users?.size ?: 0).toString() + " accounts",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            StatusPill(
                text = if (nurses) "Clinic directory" else "Read-only access",
                tone = StatusTone.INFO,
            )
        }

        users?.forEach { user ->
            print(user)
            PersonRow(
                initials = initials(user.name),
                name = user.name,
                subtitle = when(user.role) {
                    UserRole.PATIENT -> "Patient"
                    UserRole.DENTIST -> "Dentist"
                    UserRole.NURSE -> "Nurse"
                },
                onClick = { onPatientClick(user.id) },
                trailing = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
            )
        }

        if (users.isNullOrEmpty()) {
            EmptyState(
                title = if (nurses) "No accounts found" else "No matching patients",
                message = if (nurses) {
                    "Accounts will appear here once they are registered."
                } else {
                    "Try another name or NRIC. Patient records stay read-only."
                },
                icon = Icons.Filled.Person,
            )
        }

        if (nurses) {
            NoticeCard(
                text = "Only nurses can create, edit or remove staff accounts.",
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

