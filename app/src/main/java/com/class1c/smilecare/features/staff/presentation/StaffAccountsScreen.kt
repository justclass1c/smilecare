package com.class1c.smilecare.features.staff.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.navigation.Route
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.PersonRow
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTabs
import com.class1c.smilecare.ui.components.SmileTextField
import com.class1c.smilecare.ui.components.StatusPill
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.SuccessBanner

/**
 * Reception staff directory. The success variant is controlled by a flag so
 * the updated-account confirmation reuses this destination.
 */
@Composable
fun StaffAccountsScreen(
    selectedTab: Int = 0,
    showSuccess: Boolean = false,
    onTabChange: (Int) -> Unit = {},
    onAccountClick: (String) -> Unit = {},
    onAddClick: () -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    SmileScreen(
        role = com.class1c.smilecare.domain.model.user.UserRole.NURSE,
        currentRoute = Route.StaffAccounts,
        title = "User management",
        onNavigate = onNavigate,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddClick,
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
        },
    ) {
        if (showSuccess) {
            SuccessBanner("User updated successfully")
        }

        Text(
            text = "Manage clinic access with clarity and care.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SmileTabs(
            options = listOf("Patients", "Dentists", "Staff"),
            selectedIndex = selectedTab,
            onSelect = onTabChange,
        )
        SmileTextField(
            label = "Search name or phone",
            value = "",
            placeholder = "Search",
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "0 accounts",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            StatusPill(text = "Clinic directory", tone = StatusTone.INFO)
        }

        directoryPeople.forEach { person ->
            PersonRow(
                initials = person.initials,
                name = person.name,
                subtitle = person.roleLabel,
                onClick = { onAccountClick(person.id) },
                trailing = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
            )
        }

        NoticeCard(
            text = "Staff accounts control who can access clinical and billing tools.",
            icon = Icons.Filled.People,
        )
    }
}

private data class DirectoryPerson(
    val id: String,
    val initials: String,
    val name: String,
    val roleLabel: String,
)

private val directoryPeople = listOf(
    DirectoryPerson("staff-1", "DN", "Staff name", "Dentist · Active"),
    DirectoryPerson("staff-2", "DN", "Staff name", "Dentist · Active"),
    DirectoryPerson("staff-3", "RC", "Staff name", "Reception · Active"),
    DirectoryPerson("staff-4", "RC", "Staff name", "Reception · Active"),
)
