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
import com.class1c.smilecare.features.auth.register.presentation.RegisterViewModel
import com.class1c.smilecare.features.home.presentation.HomeScreen
import com.class1c.smilecare.features.inventory.equipmentdetail.presentation.EquipmentDetailScreen
import com.class1c.smilecare.features.inventory.equipmentdetail.presentation.EquipmentDetailViewModel
import com.class1c.smilecare.features.inventory.equipmentform.presentation.EquipmentFormScreen
import com.class1c.smilecare.features.inventory.equipmentform.presentation.EquipmentFormViewModel
import com.class1c.smilecare.features.inventory.inventorydetail.presentation.InventoryItemDetailScreen
import com.class1c.smilecare.features.inventory.inventorydetail.presentation.InventoryItemDetailViewModel
import com.class1c.smilecare.features.inventory.itemform.presentation.ItemFormScreen
import com.class1c.smilecare.features.inventory.itemform.presentation.ItemFormViewModel
import com.class1c.smilecare.features.inventory.presentation.EquipmentScreen
import com.class1c.smilecare.features.inventory.presentation.EquipmentViewModel
import com.class1c.smilecare.features.inventory.presentation.InventoryScreen
import com.class1c.smilecare.features.inventory.presentation.InventoryViewModel
import com.class1c.smilecare.features.lucky.presentation.LuckyDrawScreen
import com.class1c.smilecare.features.manage.presentation.ManageClinicScreen
import com.class1c.smilecare.features.manage.presentation.ManageClinicViewModel
import com.class1c.smilecare.features.payments.presentation.PaymentsScreen
import com.class1c.smilecare.features.patients.presentation.PatientDetailScreen
import com.class1c.smilecare.features.user.presentation.UsersScreen
import com.class1c.smilecare.features.profile.presentation.DeleteAccountScreen
import com.class1c.smilecare.features.profile.presentation.EditProfileScreen
import com.class1c.smilecare.features.profile.presentation.ProfileScreen
import com.class1c.smilecare.features.profile.presentation.ProfileViewModel
import com.class1c.smilecare.features.schedule.presentation.ScheduleScreen
import com.class1c.smilecare.features.services.presentation.DeleteServiceScreen
import com.class1c.smilecare.features.services.servicedetail.presentation.ServiceDetailScreen
import com.class1c.smilecare.features.services.servicesformscreen.presentation.ServiceFormScreen
import com.class1c.smilecare.features.servicesscreen.presentation.ServicesScreen
import com.class1c.smilecare.features.services.servicesformscreen.presentation.ServiceFormViewModel
import com.class1c.smilecare.features.servicesscreen.presentation.ServicesScreenViewModel
import com.class1c.smilecare.features.staff.presentation.DeleteStaffAccountScreen
import com.class1c.smilecare.features.staff.presentation.UserAccountDetailsScreen
import com.class1c.smilecare.features.staff.accountformscreen.presentation.StaffAccountFormScreen
import com.class1c.smilecare.features.staff.accountformscreen.presentation.StaffAccountFormViewModel
import com.class1c.smilecare.features.staff.presentation.UserAccountDetailsViewModel
import com.class1c.smilecare.features.user.presentation.UsersViewModel

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
                if (state.loginSuccessful) {
                    navController.navigate(Route.Home.path) {
                        popUpTo(Route.Login.path) { inclusive = true }
                    }

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
                        UserRole.NURSE-> {
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
            val viewModel: UsersViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            UsersScreen(
                role = role,
                state = state,
                event = viewModel::onEvent,
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
            val viewModel: ServicesScreenViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            ServicesScreen(
                state = state,
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
            val viewModel: ServiceFormViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val serviceId = backStackEntry.arguments
                ?.getString(Route.ServiceForm.ID_ARG)
                ?: "new"
            ServiceFormScreen(
                state = state,
                onEvent = viewModel::onEvent,
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
            val viewModel: UsersViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            UsersScreen(
                role = role,
                state = state,
                event = viewModel::onEvent,
                onPatientClick = { userId ->
                    navController.navigate(Route.UserAccountDetails.create(userId))
                },
                onAddStaffClick = {
                    navController.navigate(Route.StaffAccountForm.create("new"))
                },
                onNavigate = navigateToTab,
            )
        }

        composable(Route.UserAccountDetails.path) {
            val viewModel: UserAccountDetailsViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            UserAccountDetailsScreen(
                state = state,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = {
                    navController.navigate(Route.StaffAccountForm.create(state.userId))
                },
                onDeleteClick = {
                    navController.navigate(Route.DeleteStaffAccount.create(state.userId))
                },
            )
        }

        composable(Route.StaffAccountForm.path) { backStackEntry ->
            val viewModel: StaffAccountFormViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val staffId = backStackEntry.arguments
                ?.getString(Route.StaffAccountForm.ID_ARG)
                ?: "new"
            StaffAccountFormScreen(
                state = state,
                onEvent = viewModel::onEvent,
                isEditing = staffId != "new",
                onBackClick = {
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
            val viewModel: InventoryViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(Unit) {
                viewModel.refresh()
            }
            InventoryScreen(
                items = state.items,
                isLoading = state.isLoading,
                errorMessage = state.errorMessage,
                onItemClick = { itemName ->
                    navController.navigate(Route.InventoryDetail.create(itemName))
                },
                onAddClick = {
                    navController.navigate(Route.ItemForm.create())
                },
                onRetryClick = viewModel::refresh,
                onBackClick = {
                    navigateToTab(Route.ManageClinic)
                },
                onNavigate = navigateToTab,
            )
        }

        composable(Route.ItemForm.path) {
            val viewModel: ItemFormViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            ItemFormScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = {
                    navController.popBackStack()
                },
                onSaved = {
                    navController.popBackStack()
                },
            )
        }

        composable(Route.InventoryDetail.path) { backStackEntry ->
            val viewModel: InventoryItemDetailViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val itemName = backStackEntry.arguments
                ?.getString(Route.InventoryDetail.NAME_ARG)
                .orEmpty()
            InventoryItemDetailScreen(
                itemName = itemName,
                state = state,
                onEvent = viewModel::onEvent,
                onLoad = viewModel::load,
                onBackClick = {
                    navController.popBackStack()
                },
                onFinished = {
                    navController.popBackStack()
                },
            )
        }

        composable(Route.Equipment.path) {
            val viewModel: EquipmentViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(Unit) {
                viewModel.refresh()
            }
            EquipmentScreen(
                items = state.items,
                isLoading = state.isLoading,
                errorMessage = state.errorMessage,
                onEquipmentClick = { equipmentName ->
                    navController.navigate(Route.EquipmentDetail.create(equipmentName))
                },
                onAddClick = {
                    navController.navigate(Route.EquipmentForm.create())
                },
                onRetryClick = viewModel::refresh,
                onBackClick = {
                    navigateToTab(Route.ManageClinic)
                },
                onNavigate = navigateToTab,
            )
        }

        composable(Route.EquipmentDetail.path) { backStackEntry ->
            val viewModel: EquipmentDetailViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val equipmentName = backStackEntry.arguments
                ?.getString(Route.EquipmentDetail.NAME_ARG)
                .orEmpty()
            EquipmentDetailScreen(
                equipmentName = equipmentName,
                state = state,
                onEvent = viewModel::onEvent,
                onLoad = viewModel::load,
                onBackClick = {
                    navController.popBackStack()
                },
                onFinished = {
                    navController.popBackStack()
                },
            )
        }

        composable(Route.EquipmentForm.path) {
            val viewModel: EquipmentFormViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            EquipmentFormScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onBackClick = {
                    navController.popBackStack()
                },
                onSaved = {
                    navController.popBackStack()
                },
            )
        }

        composable(Route.ManageClinic.path) {
            val viewModel: ManageClinicViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(Unit) {
                viewModel.refresh()
            }
            ManageClinicScreen(
                inventoryCount = state.inventoryCount,
                equipmentCount = state.equipmentCount,
                isLoading = state.isLoading,
                errorMessage = state.errorMessage,
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
            val viewModel: ProfileViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            ProfileScreen(
                state = state,
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
