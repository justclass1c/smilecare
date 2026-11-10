package com.class1c.smilecare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.features.appointments.presentation.AppointmentDetailScreen
import com.class1c.smilecare.features.appointments.presentation.AppointmentsScreen
import com.class1c.smilecare.features.appointments.presentation.BookAppointmentScreen
import com.class1c.smilecare.features.appointments.presentation.CancelAppointmentScreen
import com.class1c.smilecare.features.appointments.presentation.CompleteAppointmentScreen
import com.class1c.smilecare.features.appointments.presentation.FindNextAvailableScreen
import com.class1c.smilecare.features.appointments.presentation.ReminderPreviewScreen
import com.class1c.smilecare.features.appointments.presentation.RemindersScreen
import com.class1c.smilecare.features.appointments.presentation.RescheduleAppointmentScreen
import com.class1c.smilecare.features.auth.login.presentation.LoginScreen
import com.class1c.smilecare.features.auth.login.presentation.LoginViewModel
import com.class1c.smilecare.features.auth.register.presentation.RegisterScreen
import com.class1c.smilecare.features.auth.presentation.SplashScreen
import com.class1c.smilecare.features.auth.register.presentation.RegisterUIState
import com.class1c.smilecare.features.auth.register.presentation.RegisterViewModel
import com.class1c.smilecare.features.home.presentation.HomeScreen
import com.class1c.smilecare.features.inventory.presentation.EquipmentScreen
import com.class1c.smilecare.features.inventory.presentation.InventoryScreen
import com.class1c.smilecare.features.lucky.presentation.LuckyDrawScreen
import com.class1c.smilecare.features.manage.presentation.ManageClinicScreen
import com.class1c.smilecare.features.payments.presentation.PaymentsScreen
import com.class1c.smilecare.features.patients.presentation.PatientDetailScreen
import com.class1c.smilecare.features.patients.presentation.PatientsScreen
import com.class1c.smilecare.features.profile.presentation.DeleteAccountScreen
import com.class1c.smilecare.features.profile.presentation.EditProfileScreen
import com.class1c.smilecare.features.profile.presentation.ProfileScreen
import com.class1c.smilecare.features.schedule.presentation.ScheduleScreen
import com.class1c.smilecare.features.services.presentation.DeleteServiceScreen
import com.class1c.smilecare.features.services.presentation.ServiceDetailScreen
import com.class1c.smilecare.features.services.presentation.ServiceFormScreen
import com.class1c.smilecare.features.services.presentation.ServicesScreen
import com.class1c.smilecare.features.staff.presentation.DeleteStaffAccountScreen
import com.class1c.smilecare.features.staff.presentation.StaffAccountDetailScreen
import com.class1c.smilecare.features.staff.presentation.StaffAccountFormScreen
import com.class1c.smilecare.features.staff.presentation.StaffAccountsScreen

/**
 * Single place where features are glued to routes. The role is supplied here
 * so one set of screens can serve every account type.
 */
@Composable
fun SmileCareNavHost(
    modifier: Modifier = Modifier,
    role: UserRole = UserRole.PATIENT,
    navController: NavHostController = rememberNavController(),
) {
    val navigateToTab: (Route) -> Unit = { route ->
        navController.navigate(route.path) {
            popUpTo(Route.Home.path) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = Route.Splash.path,
        modifier = modifier,
    ) {
        composable(Route.Splash.path) {
            SplashScreen(
                onContinueClick = {
                    navController.navigate(Route.Login.path) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
            )
        }

        composable(Route.Login.path) {
            val viewModel: LoginViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            LoginScreen(
                state = state,
                onEvent = viewModel::onEvent,
                showError = state.loginError != null,
                onRegisterClick = {
                    navController.navigate(Route.Register.path)
                },
            )

            LaunchedEffect(state.loginSuccessful) {
                if (state.loginSuccessful) navController.navigate(Route.Home.path) {
                    popUpTo(Route.Login.path) { inclusive = true }
                }
            }
        }

        composable(Route.Register.path) {
            val viewModel: RegisterViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            RegisterScreen(
                state = state,
                role = role,
                onEvent = viewModel::onEvent,
                showError = false,
                showSuccess = false,
                onRegisterClick = { },
                onBackClick = {
                    navController.popBackStack()
                },
            )

            LaunchedEffect(state.isRegistered) {
                if (state.isRegistered) navController.popBackStack(Route.Login.path, inclusive = false)
            }
        }

        composable(Route.Home.path) {
            HomeScreen(
                role = role,
                onBookClick = {
                    navController.navigate(Route.BookAppointment.create(1))
                },
                onAppointmentsClick = {
                    navigateToTab(Route.Appointments)
                },
                onPrimaryQuickActionClick = {
                    when (role) {
                        UserRole.PATIENT -> {
                            navController.navigate(Route.BookAppointment.create(1))
                        }
                        UserRole.DENTIST -> {
                            navigateToTab(Route.Schedule)
                        }
                        UserRole.RECEPTIONIST -> {
                            navigateToTab(Route.Appointments)
                        }
                    }
                },
                onSecondaryQuickActionClick = {
                    navigateToTab(Route.Services)
                },
                onNavigate = navigateToTab,
            )
        }

        composable(Route.Appointments.path) {
            AppointmentsScreen(
                role = role,
                onAppointmentClick = { appointmentId ->
                    navController.navigate(Route.AppointmentDetail.create(appointmentId))
                },
                onBookClick = {
                    navController.navigate(Route.BookAppointment.create(1))
                },
                onRemindersClick = {
                    navController.navigate(Route.Reminders.path)
                },
                onFindNextClick = {
                    navController.navigate(Route.FindNextAvailable.path)
                },
                onNavigate = navigateToTab,
            )
        }

        composable(Route.AppointmentDetail.path) { backStackEntry ->
            AppointmentDetailScreen(
                role = role,
                appointmentId = backStackEntry.arguments
                    ?.getString(Route.AppointmentDetail.ID_ARG)
                    ?: "APT-0000",
                onBackClick = {
                    navController.popBackStack()
                },
                onRescheduleClick = {
                    navController.navigate(Route.BookAppointment.create(2))
                },
                onCancelClick = {
                    navController.popBackStack()
                },
                onCompleteClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(Route.BookAppointment.path) { backStackEntry ->
            val step = backStackEntry.arguments
                ?.getString(Route.BookAppointment.STEP_ARG)
                ?.toIntOrNull()
                ?: 1
            BookAppointmentScreen(
                role = role,
                step = step,
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = {
                    navController.navigate(Route.BookAppointment.create(step + 1))
                },
                onConfirmClick = {
                    navController.navigate(Route.Appointments.path) {
                        popUpTo(Route.Home.path)
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(Route.CompleteAppointment.path) {
            CompleteAppointmentScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onConfirmClick = {},
            )
        }

        composable(Route.FindNextAvailable.path) {
            FindNextAvailableScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onFindClick = {},
                onBookClick = {},
            )
        }

        composable(Route.Reminders.path) {
            RemindersScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPreviewClick = {
                    navController.navigate(Route.ReminderPreview.path)
                },
            )
        }

        composable(Route.ReminderPreview.path) {
            ReminderPreviewScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onViewClick = {},
            )
        }

        composable(Route.RescheduleAppointment.path) { backStackEntry ->
            val appointmentId = backStackEntry.arguments
                ?.getString(Route.RescheduleAppointment.ID_ARG)
                ?: "APT-0000"
            RescheduleAppointmentScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(Route.CancelAppointment.path) { backStackEntry ->
            val appointmentId = backStackEntry.arguments
                ?.getString(Route.CancelAppointment.ID_ARG)
                ?: "APT-0000"
            CancelAppointmentScreen(
                role = role,
                onBackClick = {
                    navController.popBackStack()
                },
                onKeepClick = {
                    navController.popBackStack()
                },
                onConfirmClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(Route.Schedule.path) {
            ScheduleScreen(
                role = role,
                onAppointmentClick = { appointmentId ->
                    navController.navigate(Route.AppointmentDetail.create(appointmentId))
                },
                onNavigate = navigateToTab,
            )
        }

        composable(Route.Patients.path) {
            PatientsScreen(
                role = role,
                onPatientClick = { patientId ->
                    navController.navigate(Route.PatientDetail.create(patientId))
                },
                onAddStaffClick = {
                    navController.navigate(Route.StaffAccountForm.create("new"))
                },
                onNavigate = navigateToTab,
            )
        }

        composable(Route.PatientDetail.path) { backStackEntry ->
            val patientId = backStackEntry.arguments
                ?.getString(Route.PatientDetail.ID_ARG)
                ?: "patient-1"
            PatientDetailScreen(
                role = role,
                patientId = patientId,
                onBackClick = {
                    navController.popBackStack()
                },
                onAppointmentClick = { appointmentId ->
                    navController.navigate(Route.AppointmentDetail.create(appointmentId))
                },
            )
        }

        composable(Route.Payments.path) {
            PaymentsScreen(
                role = role,
                onPayClick = {},
                onInvoiceClick = { invoiceId -> },
                onFilterClick = {},
                onNavigate = navigateToTab,
            )
        }

        composable(Route.Services.path) {
            ServicesScreen(
                role = role,
                onServiceClick = { serviceId ->
                    navController.navigate(Route.ServiceDetail.create(serviceId))
                },
                onAddServiceClick = {
                    navController.navigate(Route.ServiceDetail.create("new"))
                },
                onNavigate = navigateToTab,
            )
        }

        composable(Route.ServiceDetail.path) {
            ServiceDetailScreen(
                role = role,
                onBackClick = {
                    navController.popBackStack()
                },
                onBookClick = {
                    navController.navigate(Route.BookAppointment.create(1))
                },
                onEditClick = {
                    navController.navigate(Route.ServiceForm.create("new"))
                },
                onToggleActiveClick = {},
                onDeleteClick = {
                    navController.navigate(Route.DeleteService.create("new"))
                },
            )
        }

        composable(Route.ServiceForm.path) { backStackEntry ->
            val serviceId = backStackEntry.arguments
                ?.getString(Route.ServiceForm.ID_ARG)
                ?: "new"
            ServiceFormScreen(
                isEditing = serviceId != "new",
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(Route.DeleteService.path) { backStackEntry ->
            val serviceId = backStackEntry.arguments
                ?.getString(Route.DeleteService.ID_ARG)
                ?: "new"
            DeleteServiceScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onKeepClick = {
                    navController.popBackStack()
                },
                onDeactivateClick = {},
                onDeleteClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(Route.StaffAccounts.path) {
            StaffAccountsScreen(
                onAccountClick = { staffId ->
                    navController.navigate(Route.StaffAccountDetail.create(staffId))
                },
                onAddClick = {
                    navController.navigate(Route.StaffAccountForm.create("new"))
                },
                onNavigate = navigateToTab,
            )
        }

        composable(Route.StaffAccountDetail.path) { backStackEntry ->
            val staffId = backStackEntry.arguments
                ?.getString(Route.StaffAccountDetail.ID_ARG)
                ?: "staff-1"
            StaffAccountDetailScreen(
                staffId = staffId,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = {
                    navController.navigate(Route.StaffAccountForm.create(staffId))
                },
                onDeleteClick = {
                    navController.navigate(Route.DeleteStaffAccount.create(staffId))
                },
            )
        }

        composable(Route.StaffAccountForm.path) { backStackEntry ->
            val staffId = backStackEntry.arguments
                ?.getString(Route.StaffAccountForm.ID_ARG)
                ?: "new"
            StaffAccountFormScreen(
                isEditing = staffId != "new",
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(Route.DeleteStaffAccount.path) { backStackEntry ->
            val staffId = backStackEntry.arguments
                ?.getString(Route.DeleteStaffAccount.ID_ARG)
                ?: "staff-1"
            DeleteStaffAccountScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                },
                onDeleteClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(Route.Inventory.path) {
            InventoryScreen(
                onItemClick = { itemId -> },
                onAddClick = {},
                onNavigate = navigateToTab,
            )
        }

        composable(Route.Equipment.path) {
            EquipmentScreen(
                onEquipmentClick = { equipmentId -> },
                onAddClick = {},
                onNavigate = navigateToTab,
            )
        }

        composable(Route.ManageClinic.path) {
            ManageClinicScreen(
                onUsersClick = {
                    navigateToTab(Route.StaffAccounts)
                },
                onServicesClick = {
                    navigateToTab(Route.Services)
                },
                onInventoryClick = {
                    navigateToTab(Route.Inventory)
                },
                onEquipmentClick = {
                    navigateToTab(Route.Equipment)
                },
                onLuckyDrawClick = {
                    navController.navigate(Route.LuckyDraw.path)
                },
                onNavigate = navigateToTab,
            )
        }

        composable(Route.LuckyDraw.path) {
            LuckyDrawScreen(
                onManageClick = {
                    navController.popBackStack()
                },
                onNavigate = navigateToTab,
            )
        }

        composable(Route.Profile.path) {
            ProfileScreen(
                role = role,
                onEditProfileClick = {
                    navController.navigate(Route.EditProfile.path)
                },
                onChangePasswordClick = {},
                onDeleteAccountClick = {},
                onConfirmDeleteClick = {},
                onLogoutClick = {
                    navController.navigate(Route.Login.path) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onNavigate = navigateToTab,
            )
        }

        composable(Route.EditProfile.path) {
            EditProfileScreen(
                role = role,
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(Route.DeleteAccount.path) {
            DeleteAccountScreen(
                role = role,
                onBackClick = {
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                },
                onDeleteClick = {
                    navController.navigate(Route.Login.path) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
            )
        }
    }
}
