package com.class1c.smilecare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.features.appointments.presentation.AppointmentDetailScreen
import com.class1c.smilecare.features.appointments.presentation.AppointmentsScreen
import com.class1c.smilecare.features.appointments.presentation.BookAppointmentScreen
import com.class1c.smilecare.features.auth.presentation.LoginScreen
import com.class1c.smilecare.features.auth.presentation.RegisterScreen
import com.class1c.smilecare.features.home.presentation.HomeScreen
import com.class1c.smilecare.features.profile.presentation.ProfileScreen
import com.class1c.smilecare.features.services.presentation.ServiceDetailScreen
import com.class1c.smilecare.features.services.presentation.ServicesScreen

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
        startDestination = Route.Login.path,
        modifier = modifier,
    ) {
        composable(Route.Login.path) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Route.Home.path) {
                        popUpTo(Route.Login.path) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Route.Register.path)
                },
            )
        }

        composable(Route.Register.path) {
            RegisterScreen(
                role = role,
                onRegisterClick = {
                    navController.popBackStack(Route.Login.path, inclusive = false)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
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
                    if (role == UserRole.PATIENT) {
                        navController.navigate(Route.BookAppointment.create(1))
                    } else {
                        navigateToTab(Route.Appointments)
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
                onFindNextClick = {
                    navController.navigate(Route.BookAppointment.create(2))
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
            )
        }

        composable(Route.Profile.path) {
            ProfileScreen(
                role = role,
                onLogoutClick = {
                    navController.navigate(Route.Login.path) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onNavigate = navigateToTab,
            )
        }
    }
}
